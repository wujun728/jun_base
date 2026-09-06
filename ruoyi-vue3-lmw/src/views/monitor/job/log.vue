<script setup name="JobLog">
import getSearchConfig from './config/logSearch.js'
import getContentConfig from './config/logContent.js'
import getComputedConfig from '@/hooks/getPageConfig'
import LogView from './cpns/LogView.vue'
import { monitorBaseUrl } from '@/api/config/base.js'
import { getInfo } from '@/api/business/main/index.js'
import to from '@/utils/to.js'
const route = useRoute()
const jobId = route.params.jobId
const cacheKey = jobId
const proxy = inject('proxy')
const { sys_common_status, sys_job_group } = proxy.useDict(
  'sys_common_status',
  'sys_job_group'
)

const pageName = 'jobLog'
const requestBaseUrl = monitorBaseUrl
const otherRequestOption = ref({
  jobId: jobId ?? 0,
})

const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const descConfig = ref({})

const tableHideItems = ref([])
const dictMap = {
  status: sys_common_status,
  jobGroup: sys_job_group,
}
const searchConfig = getSearchConfig()
const searchConfigComputed = computed(() => {
  return getComputedConfig(searchConfig, dictMap)
})
const searchData = computed(() => {
  return pageContentRef.value?.finalSearchData
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
const beforeSend = (queryInfo) => {
  if (queryInfo.dateRange && Array.isArray(queryInfo.dateRange)) {
    const dateRange = queryInfo.dateRange
    queryInfo['params[beginTime]'] = dateRange[0]
    queryInfo['params[endTime]'] = dateRange[1]
    delete queryInfo.dateRange
  }
}
const headerButtons = ['refresh', 'delete', 'columnDisplay', 'comSearch']

const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
}

const handleClose = () => {
  const obj = { path: '/monitor/job' }
  proxy.$tab.closeOpenPage(obj)
}

const viewFormData = ref({})
const viewDialog = ref(false)
const handleView = (backData) => {
  viewFormData.value = backData
  viewDialog.value = true
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy.download(
    'monitor/jobLog/export',
    {
      ...searchData.value,
    },
    `job_log_${new Date().getTime()}.xlsx`
  )
}
const getJobInfo = async () => {
  const [res] = await to(getInfo('/monitor/job/' + jobId))
  if (res?.data) {
    const data = res.data
    pageSearchRef.value.setFormData('jobName', data.jobName)
    pageSearchRef.value.setFormData('jobGroup', data.jobGroup)
  }
  pageSearchRef.value.search()
}
getJobInfo()
</script>
<template>
  <div class="default-main page">
    <PageSearch
      ref="pageSearchRef"
      :pageName="pageName"
      :otherRequestOption="otherRequestOption"
      :searchConfig="searchConfigComputed"
      :cacheKey="cacheKey"
    ></PageSearch>
    <PageContent
      ref="pageContentRef"
      :pageName="pageName"
      :contentConfig="contentConfigComputed"
      :descConfig="descConfig"
      :dictMap="dictMap"
      :tableListener="tableListener"
      :tableSelected="tableSelected"
      :headerButtons="headerButtons"
      :showEdit="false"
      :showDelete="false"
      :requestBaseUrl="requestBaseUrl"
      :otherRequestOption="otherRequestOption"
      :autoSend="false"
      :cacheKey="cacheKey"
      @beforeSend="beforeSend"
      @onChangeShowColumn="onChangeShowColumn"
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
        <el-button class="order17 ml12" @click="handleClose" type="warning">
          <SvgIcon size="14" iconClass="times" />
          <span class="ml6">关闭</span>
        </el-button>
      </template>
      <template #doSth="{ backData }">
        <el-button
          size="small"
          type="primary"
          @click="handleView(backData)"
          v-hasPermi="['monitor:job:query']"
        >
          <SvgIcon size="12" iconClass="eye" />
          <span class="ml6">详细</span>
        </el-button>
      </template>
      <template #statusSlot="{ backData }">
        <dict-tag :options="sys_common_status" :value="backData.status" />
      </template>
      <template #jobGroupSlot="{ backData }">
        <dict-tag :options="sys_job_group" :value="backData.jobGroup" />
      </template>
      <template #createTimeSlot="{ backData }">
        <span>{{ parseTime(backData.createTime) }}</span>
      </template>
    </PageContent>
    <LogView
      v-model:dialogVisible="viewDialog"
      :viewFormData="viewFormData"
    ></LogView>
  </div>
</template>

<style scoped lang="scss">
.page {
  :deep(.del) {
    margin-top: 6px;
  }
}
</style>
