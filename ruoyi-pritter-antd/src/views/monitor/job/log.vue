<script setup name="JobLog">
import getSearchConfig from './config/logSearch.js'
import getContentConfig from './config/logContent.js'
import getComputedConfig from '@/hooks/getPageConfig'
import LogView from './components/LogView.vue'
import { monitorBaseUrl } from '@/api/config/base.js'
import { getInfo } from '@/api/business/main/index.js'
import to from '@/utils/to'
import { jobLog } from '@/views/pageName.js'

const route = useRoute()
const proxy = inject('proxy')
const jobId = route.params.jobId
const cacheKey = jobId
const { sys_common_status, sys_job_group } = proxy.useDict(
  'sys_common_status',
  'sys_job_group'
)

const pageName = jobLog
const requestBaseUrl = monitorBaseUrl
const otherRequestOption = ref({
  jobId: jobId ?? 0,
})

const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const tableHideItems = ref([])

const dictMap = {
  status: sys_common_status,
  jobGroup: sys_job_group,
}

const searchConfig = getSearchConfig()
const searchConfigComputed = computed(() => {
  return getComputedConfig(searchConfig, dictMap)
})

const searchData = computed(() => pageContentRef.value?.finalSearchData)

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
  proxy.$tab.closeOpenPage({ path: '/monitor/job' })
}

const viewFormData = ref({})
const viewOpen = ref(false)

const handleView = (backData) => {
  viewFormData.value = backData
  viewOpen.value = true
}

const handleExport = () => {
  proxy.download(
    'monitor/jobLog/export',
    { ...searchData.value },
    `job_log_${new Date().getTime()}.xlsx`
  )
}

const getJobInfo = async () => {
  const [res] = await to(getInfo(`/monitor/job/${jobId}`))
  if (res?.data) {
    pageSearchRef.value.setFormData('jobName', res.data.jobName)
    pageSearchRef.value.setFormData('jobGroup', res.data.jobGroup)
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
    />
    <PageContent
      ref="pageContentRef"
      :pageName="pageName"
      :contentConfig="contentConfigComputed"
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
      :tableHideItems="tableHideItems"
      @beforeSend="beforeSend"
      @onChangeShowColumn="onChangeShowColumn"
    >
      <template #handleLeft>
        <a-button
          class="order16"
          type="dashed"
          v-hasPermi="['monitor:job:export']"
          @click="handleExport"
        >
          <template #icon>
            <SvgIcon size="14" iconClass="export" />
          </template>
          <span class="ml6">导出</span>
        </a-button>
        <a-button class="order17" type="dashed" @click="handleClose">
          <template #icon>
            <SvgIcon size="14" iconClass="close" />
          </template>
          <span class="ml6">关闭</span>
        </a-button>
      </template>
      <template #doSth="{ backData }">
        <a-button
          size="small"
          type="primary"
          v-hasPermi="['monitor:job:query']"
          @click="handleView(backData)"
        >
          <template #icon>
            <SvgIcon size="12" iconClass="eye-open" />
          </template>
          <span class="ml6">详细</span>
        </a-button>
      </template>
      <template #statusSlot="{ backData }">
        <DictTag :options="sys_common_status" :value="backData.status" />
      </template>
      <template #jobGroupSlot="{ backData }">
        <DictTag :options="sys_job_group" :value="backData.jobGroup" />
      </template>
    </PageContent>
    <LogView v-model:open="viewOpen" :view-form-data="viewFormData" />
  </div>
</template>

<style scoped lang="scss"></style>
