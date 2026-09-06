<script setup name="Online">
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig.js'
import getComputedConfig from '@/hooks/getPageConfig'
import { systemBaseUrl } from '@/api/config/base.js'
import { forceLogout } from '@/api/monitor/online.js'
import to from '@/utils/to'

const proxy = inject('proxy')

const pageName = ref('online')
const requestBaseUrl = systemBaseUrl
const pageSearchRef = ref(null)
const pageContentRef = ref(null)
const descConfig = ref({})
const tableHideItems = ref([])
const headerButtons = ['refresh', 'columnDisplay', 'comSearch']
const dictMap = {}
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

const search = () => {
  pageSearchRef.value?.search()
}

const beforeSend = (queryInfo) => {}

const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
}
const handleForceLogout = async (row) => {
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
      :descConfig="descConfig"
      :dictMap="dictMap"
      :tableListener="tableListener"
      :tableSelected="tableSelected"
      :requestBaseUrl="requestBaseUrl"
      :headerButtons="headerButtons"
      @beforeSend="beforeSend"
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
  :deep(.del) {
    margin-left: 12px;
  }
}
</style>
