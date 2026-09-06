<script setup name="Online" lang="ts">
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig'
import getComputedConfig from '@/hooks/getPageConfig'
import { monitorBaseUrl } from '@/api/config/base'
import { forceLogout } from '@/api/monitor/online'
import to from '@/utils/to'
import { online } from '@/views/pageName.ts'
import { parseTime } from '@/utils/hsj/timeFormat'
import { proxy } from '@/utils/provide'

const pageName = online
const requestBaseUrl = monitorBaseUrl
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const tableHideItems = ref<string[]>([])
const headerButtons: ButtonsType[] = ['refresh', 'columnDisplay', 'comSearch']
const dictMap = {}
const searchConfig = getSearchConfig()
const searchConfigComputed = computed(() => {
  return getComputedConfig(searchConfig, dictMap)
})
const tableSelected = ref<OnlineItem[]>([])
const tableListener = {
  selectionChange: (selected: OnlineItem[]) => {
    tableSelected.value = selected
  },
}
const contentConfig = getContentConfig()
const contentConfigComputed = computed(() => {
  contentConfig.hideItems = tableHideItems
  return contentConfig
})

const search = () => {
  pageSearchRef.value?.search()
}

const onChangeShowColumn = (filterArr: string[]) => {
  tableHideItems.value = filterArr
}
const handleForceLogout = async (row: OnlineItem) => {
  const [res] = await to(forceLogout(row.tokenId))
  if (res) {
    search()
    proxy.$modal.notifySuccess('强退成功')
  }
}

const init = () => {}

init()
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
      :requestBaseUrl="requestBaseUrl"
      :headerButtons="headerButtons"
      @onChangeShowColumn="onChangeShowColumn"
    >
      <template #loginTimeSlot="{ backData }">
        <span>{{ parseTime(backData.loginTime) }}</span>
      </template>
      <template #doSth="{ backData }">
        <el-popconfirm
          title="是否强退当前选中的用户？"
          @confirm="handleForceLogout(backData)"
        >
          <template #reference>
            <el-button
              type="primary"
              size="small"
              v-hasPermi="['monitor:online:forceLogout']"
            >
              <SvgIcon size="14" iconClass="sign-out-alt" />
              <span class="ml6">强退</span>
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
}
</style>
