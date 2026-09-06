<script setup name="Operlog" lang="ts">
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig'
import getComputedConfig from '@/hooks/getPageConfig'
import getDialogConfig from './config/dialogConfig'
import { monitorBaseUrl } from '@/api/config/base.ts'
import { operlog } from '@/views/pageName.ts'
import { proxy } from '@/utils/provide'
import { getDialogMaxHeight } from '@/utils/hsj/utils'

const { sys_oper_type, sys_common_status } = proxy.useDict(
  'sys_oper_type',
  'sys_common_status'
)
const pageName = operlog
const requestBaseUrl = monitorBaseUrl
const idKey = 'operId'
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const tableHideItems = ref<string[]>([])
const isSmall = window.isSmallScreen
const headerButtons: ButtonsType[] = [
  'refresh',
  'delete',
  'columnDisplay',
  'comSearch',
]
const dictMap = ref({
  businessType: sys_oper_type,
  status: sys_common_status,
})
const searchConfig = getSearchConfig()
const searchConfigComputed = computed(() => {
  return getComputedConfig(searchConfig, dictMap)
})
const tableSelected = ref<OperlogItem[]>([])
const tableListener = {
  selectionChange: (selected: OperlogItem[]) => {
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

const beforeSend = (queryInfo: anyObj) => {
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

const onChangeShowColumn = (filterArr: string[]) => {
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
const handleView = (row: OperlogItem) => {
  dialogVisible.value = true
  viewFormData.value = row
}
const handleClose = () => {
  dialogVisible.value = false
  viewFormData.value = {}
}
/** 操作日志类型字典翻译 */
const typeFormat = (row: anyObj) => {
  return proxy.selectDictLabel(sys_oper_type.value, row.businessType)
}

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
      class="logDialog"
      title="操作日志详细"
      v-model="dialogVisible"
      :top="isSmall ? '0vh' : '11vh'"
      :width="getWidth(1000)"
      :fullscreen="isSmall"
      @open="getMaxHeight"
      draggable
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

<style scoped lang="scss">
.page {
  :deep(.statusClass .el-radio-group) {
    width: 100%;
    .el-radio {
      margin-right: 16px;
    }
  }
  :deep(.errorMsgClass) {
    .el-form-item__content {
      margin-left: 20px !important;
    }
  }
}
.errorInfo {
  margin: 0px !important;
  font-weight: 500;
  color: var(--el-text-color-primary);
}
</style>
