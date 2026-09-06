<script setup name="Operlog">
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig.js'
import getComputedConfig from '@/hooks/getPageConfig'
import getDialogConfig from './config/dialogConfig'
import { monitorBaseUrl } from '@/api/config/base.js'
import { operlog } from '@/views/pageName.js'
import { getDialogMaxHeight } from '@/utils/hsj/utils.js'
// 操作日志
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

const searchData = computed(() => {
  return pageContentRef.value?.finalSearchData
})

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

/** 导出按钮操作 */
const handleExport = () => {
  proxy.download(
    'monitor/operlog/export',
    {
      ...searchData.value,
    },
    `monitor_${new Date().getTime()}.xlsx`
  )
}
const dialogVisible = ref(false)
const viewFormData = ref({})
const handleView = (row) => {
  dialogVisible.value = true
  viewFormData.value = row
}
const handleClose = () => {
  dialogVisible.value = false
  viewFormData.value = {}
}
/** 操作日志类型字典翻译 */
const typeFormat = (row) => {
  return proxy.selectDictLabel(sys_oper_type.value, row.businessType)
}
const isSmall = window.isSmallScreen
const maxHeight = ref(520)
const getMaxHeight = () => {
  maxHeight.value = getDialogMaxHeight('.logDialog')
}
</script>
<template>
  <div class="default-main page">
    <PageSearch
      ref="pageSearchRef"
      :pageName="pageName"
      :searchConfig="searchConfigComputed"
    ></PageSearch>
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
      @beforeSend="beforeSend"
      @onChangeShowColumn="onChangeShowColumn"
    >
      <template #handleLeft>
        <el-button
          class="order17 ml12"
          type="warning"
          v-hasPermi="['monitor:operlog:export']"
          @click="handleExport"
        >
          <SvgIcon size="14" iconClass="download" />
          <span class="ml6">导出</span>
        </el-button>
      </template>
      <template #businessTypeSlot="{ backData }">
        <dict-tag :options="sys_oper_type" :value="backData.businessType" />
      </template>
      <template #statusSlot="{ backData }">
        <dict-tag :options="sys_common_status" :value="backData.status" />
      </template>
      <template #todoSlot="{ backData }">
        <el-button
          v-hasPermi="['monitor:operlog:query']"
          type="primary"
          size="small"
          @click="handleView(backData)"
        >
          <SvgIcon size="14" iconClass="eye" />
          <span class="ml6">详情</span>
        </el-button>
      </template>
    </PageContent>
    <el-dialog
      title="操作日志详细"
      v-model="dialogVisible"
      draggable
      class="logDialog"
      :top="isSmall ? '0vh' : '11vh'"
      :fullscreen="isSmall"
      :width="getWidth(1000)"
      @open="getMaxHeight"
    >
      <el-scrollbar :max-height="maxHeight">
        <BaseForm :data="viewFormData" v-bind="dialogConfig">
          <template #titleCustom="{ backData }">
            {{ backData.formData.title }} / {{ typeFormat(backData.formData) }}
          </template>
          <template #loginInfoCustom="{ backData }">
            {{ backData.formData.operName }} / {{ backData.formData.operIp }} /
            {{ backData.formData.operLocation }}
          </template>
          <template #statusCustom="{ backData }">
            <div v-if="backData.formData.status === 0">正常</div>
            <div v-else-if="backData.formData.status === 1">失败</div>
          </template>
          <template #costTimeCustom="{ backData }">
            {{ backData.formData.costTime }}毫秒
          </template>
          <template #errorMsgCustom="{ backData }">
            <span v-if="backData.formData.status === 1">
              <span class="errorInfo">异常信息：</span>
              {{ backData.formData.errorMsg }}
            </span>
            <span v-else></span>
          </template>
        </BaseForm>
      </el-scrollbar>
      <template #footer>
        <el-button @click="handleClose">关 闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss"></style>
