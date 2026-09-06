<script setup name="Logininfor" lang="ts">
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig'
import getComputedConfig from '@/hooks/getPageConfig'
import { monitorBaseUrl } from '@/api/config/base.ts'
import { unlockLogininfor } from '@/api/monitor/logininfor'
import { getLastMonth } from '@/utils/hsj/timeFormat'
import { logininfor } from '@/views/pageName'
import { proxy } from '@/utils/provide'
import to from '@/utils/to'

const { sys_common_status } = proxy.useDict('sys_common_status')
const pageName = logininfor
const requestBaseUrl = monitorBaseUrl
const idKey = 'infoId'
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const tableHideItems = ref<string[]>([])
const headerButtons: ButtonsType[] = [
  'refresh',
  'delete',
  'columnDisplay',
  'comSearch',
]
const dictMap = ref({
  status: sys_common_status,
})
const searchConfig = getSearchConfig()
const searchConfigComputed = computed(() => {
  return getComputedConfig(searchConfig, dictMap)
})
const tableSelected = ref<LogininforItem[]>([])
const tableListener = {
  selectionChange: (selected: LogininforItem[]) => {
    tableSelected.value = selected
  },
}
const contentConfig = getContentConfig()
const contentConfigComputed = computed(() => {
  contentConfig.hideItems = tableHideItems
  return contentConfig
})

const searchData = computed(() => {
  return pageContentRef.value?.finalSearchData
})

const search = () => {
  pageSearchRef.value?.search()
}

const beforeSend = (queryInfo: anyObj) => {
  if (queryInfo.dateRange && Array.isArray(queryInfo.dateRange)) {
    const dateRange = queryInfo.dateRange
    queryInfo['params[beginTime]'] = dateRange[0]
    queryInfo['params[endTime]'] = dateRange[1]
    delete queryInfo.dateRange
  }
}

const permission = ref({
  del: 'monitor:logininfor:remove',
})

const onChangeShowColumn = (filterArr: string[]) => {
  tableHideItems.value = filterArr
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy.download(
    'monitor/logininfor/export',
    {
      ...searchData.value,
    },
    `logininfor_${new Date().getTime()}.xlsx`
  )
}

const handleUnlock = async (row: LogininforItem) => {
  const [res] = await to(unlockLogininfor(row.userName))
  if (res) {
    proxy.$modal.notifySuccess('用户' + row.userName + '解锁成功')
    search()
  }
}

onMounted(() => {
  const now = getLastMonth('YYYY-MM-DD')
  pageSearchRef.value?.setFormData('dateRange', now)
  search()
})
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
      :autoSend="false"
      @onChangeShowColumn="onChangeShowColumn"
      @beforeSend="beforeSend"
    >
      <template #handleLeft>
        <el-button
          class="order16 ml12"
          type="warning"
          v-hasPermi="['monitor:logininfor:export']"
          @click="handleExport"
        >
          <SvgIcon size="14" iconClass="download" />
          <span class="ml6">导出</span>
        </el-button>
      </template>
      <template #todoSlot="{ backData }">
        <el-popconfirm
          title="确定解锁选中记录？"
          confirm-button-text="确认"
          cancel-button-text="取消"
          confirmButtonType="primary"
          :hide-after="0"
          @confirm="handleUnlock(backData)"
        >
          <template #reference>
            <el-button
              type="primary"
              size="small"
              v-hasPermi="['monitor:logininfor:unlock']"
            >
              <SvgIcon size="12" iconClass="lock-open" />
              <span class="ml6">解锁</span>
            </el-button>
          </template>
        </el-popconfirm>
      </template>
    </PageContent>
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
