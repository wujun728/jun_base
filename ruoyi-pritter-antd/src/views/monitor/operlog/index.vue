<script setup name="Operlog">
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig.js'
import getDialogConfig from './config/dialogConfig.js'
import getComputedConfig from '@/hooks/getPageConfig'
import { monitorBaseUrl } from '@/api/config/base.js'
import { operlog } from '@/views/pageName.js'
import { getDialogMaxHeight } from '@/utils/utils'

const proxy = inject('proxy')
const { sys_oper_type, sys_common_status } = proxy.useDict(
  'sys_oper_type',
  'sys_common_status'
)
const pageName = operlog
const requestBaseUrl = monitorBaseUrl
const idKey = 'operId'
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const tableHideItems = ref([])
const headerButtons = ['refresh', 'delete', 'columnDisplay', 'comSearch']

const dictMap = {
  businessType: sys_oper_type,
  status: sys_common_status,
}

const searchConfig = getSearchConfig()
const searchConfigComputed = computed(() => {
  return getComputedConfig(searchConfig, dictMap)
})

const tableSelected = ref([])
const tableListener = {
  selectionChange: (selected) => {
    tableSelected.value = selected
  },
}

const contentConfig = getContentConfig()
const contentConfigComputed = computed(() => {
  contentConfig.hideItems = tableHideItems
  return contentConfig
})

const dialogConfig = getDialogConfig()
const searchData = computed(() => pageContentRef.value?.finalSearchData)

const search = () => {
  pageSearchRef.value?.search()
}

const beforeSend = (queryInfo) => {
  if (queryInfo.dateRange && Array.isArray(queryInfo.dateRange)) {
    const dateRange = queryInfo.dateRange
    queryInfo['params[beginTime]'] = dateRange[0]
    queryInfo['params[endTime]'] = dateRange[1]
    delete queryInfo.dateRange
  }
}

const permission = ref({
  del: 'monitor:operlog:remove',
})

const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
}

const handleExport = () => {
  proxy.download(
    'monitor/operlog/export',
    { ...searchData.value },
    `monitor_${new Date().getTime()}.xlsx`
  )
}

const detailOpen = ref(false)
const viewFormData = ref({})
const isSmall = window.isSmallScreen
const maxHeight = ref(520)

const handleView = (row) => {
  viewFormData.value = row
  detailOpen.value = true
}

const typeFormat = (row) => {
  return proxy.selectDictLabel(sys_oper_type.value, row.businessType)
}

const getMaxHeight = () => {
  maxHeight.value = getDialogMaxHeight('.log-dialog')
}
</script>

<template>
  <div class="default-main page">
    <PageSearch
      ref="pageSearchRef"
      :pageName="pageName"
      :searchConfig="searchConfigComputed"
    />
    <PageContent
      ref="pageContentRef"
      :pageName="pageName"
      :contentConfig="contentConfigComputed"
      :autoDesc="false"
      :dictMap="dictMap"
      :tableListener="tableListener"
      :tableSelected="tableSelected"
      :permission="permission"
      :requestBaseUrl="requestBaseUrl"
      :headerButtons="headerButtons"
      :showEdit="false"
      :showDelete="false"
      :idKey="idKey"
      :tableHideItems="tableHideItems"
      @beforeSend="beforeSend"
      @onChangeShowColumn="onChangeShowColumn"
    >
      <template #handleLeft>
        <a-button
          class="order16"
          type="dashed"
          v-hasPermi="['monitor:operlog:export']"
          @click="handleExport"
        >
          <template #icon>
            <SvgIcon size="14" iconClass="export" />
          </template>
          <span class="ml6">导出</span>
        </a-button>
      </template>
      <template #businessTypeSlot="{ backData }">
        <DictTag :options="sys_oper_type" :value="backData.businessType" />
      </template>
      <template #statusSlot="{ backData }">
        <DictTag :options="sys_common_status" :value="backData.status" />
      </template>
      <template #costTimeSlot="{ backData }">
        {{ backData.costTime }}毫秒
      </template>
      <template #todoSlot="{ backData }">
        <a-button
          v-hasPermi="['monitor:operlog:query']"
          type="primary"
          size="small"
          @click="handleView(backData)"
        >
          <template #icon>
            <SvgIcon size="14" iconClass="eye-open" />
          </template>
          <span class="ml6">详情</span>
        </a-button>
      </template>
    </PageContent>

    <a-modal
      v-model:open="detailOpen"
      title="操作日志详细"
      class="log-dialog"
      :width="getWidth(1000)"
      :wrap-class-name="isSmall ? 'full-modal' : ''"
      destroy-on-close
      @afterOpenChange="(open) => open && getMaxHeight()"
    >
      <div
        class="log-dialog-body"
        :style="{ maxHeight: `${maxHeight}px`, overflowY: 'auto' }"
      >
        <BaseForm :data="viewFormData" v-bind="dialogConfig" :all-disabled="true">
          <template #titleCustom="{ backData }">
            {{ backData.formData.title }} / {{ typeFormat(backData.formData) }}
          </template>
          <template #loginInfoCustom="{ backData }">
            {{ backData.formData.operName }} / {{ backData.formData.operIp }} /
            {{ backData.formData.operLocation }}
          </template>
          <template #statusCustom="{ backData }">
            {{ backData.formData.status === 0 ? '正常' : '失败' }}
          </template>
          <template #costTimeCustom="{ backData }">
            {{ backData.formData.costTime }}毫秒
          </template>
          <template #errorMsgCustom="{ backData }">
            <span v-if="backData.formData.status === 1">
              <span class="error-info">异常信息：</span>
              {{ backData.formData.errorMsg }}
            </span>
          </template>
        </BaseForm>
      </div>
      <template #footer>
        <a-button @click="detailOpen = false">关闭</a-button>
      </template>
    </a-modal>
  </div>
</template>

<style scoped lang="scss">
.error-info {
  color: #cf1322;
}
</style>
