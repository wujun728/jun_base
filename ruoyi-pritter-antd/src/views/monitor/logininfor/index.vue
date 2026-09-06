<script setup name="Operlog">
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig.js'
import getComputedConfig from '@/hooks/getPageConfig'
import { monitorBaseUrl } from '@/api/config/base.js'
import { unlockLogininfor } from '@/api/monitor/logininfor'
import { getLastMonth } from '@/utils/timeFormat'
import { logininfor } from '@/views/pageName.js'

import to from '@/utils/to'

const proxy = inject('proxy')
const { sys_common_status } = proxy.useDict('sys_common_status')
const pageName = logininfor
const requestBaseUrl = monitorBaseUrl
const idKey = 'infoId'
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const tableHideItems = ref([])
const headerButtons = ['refresh', 'delete', 'columnDisplay', 'comSearch']
const dictMap = {
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
  del: 'monitor:logininfor:remove',
})

const onChangeShowColumn = (filterArr) => {
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

const handleUnlock = async (row) => {
  row.unlockLoading = true
  const [res] = await to(unlockLogininfor(row.userName))
  if (res) {
    const msg = `用户${row.userName}解锁成功`
    proxy.$modal.msgSuccess(msg)
    search()
  }
  row.unlockLoading = false
}

onMounted(() => {
  const lastMonth = getLastMonth('YYYY-MM-DD')
  pageSearchRef.value.setFormData('dateRange', lastMonth)
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
        <a-button
          class="order16"
          type="dashed"
          v-hasPermi="['monitor:logininfor:export']"
          @click="handleExport"
        >
          <template #icon>
            <SvgIcon size="14" iconClass="export" />
          </template>
          <span class="ml6">导出</span>
        </a-button>
      </template>
      <template #todoSlot="{ backData }">
        <a-popconfirm
          title="确定解锁选中记录？"
          ok-text="确认"
          cancel-text="取消"
          @confirm="handleUnlock(backData)"
        >
          <a-button
            type="primary"
            size="small"
            v-hasPermi="['monitor:logininfor:unlock']"
            :loading="backData.unlockLoading"
          >
            <template #icon>
              <SvgIcon size="12" iconClass="lock-open" />
            </template>
            <span class="ml6">解锁</span>
          </a-button>
        </a-popconfirm>
      </template>
      <template #statusSlot="{ backData }">
        <DictTag :options="sys_common_status" :value="backData.status" />
      </template>
    </PageContent>
  </div>
</template>

<style scoped lang="scss"></style>
