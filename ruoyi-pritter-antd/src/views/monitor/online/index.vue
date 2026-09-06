<script setup name="Online">
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig.js'
import getComputedConfig from '@/hooks/getPageConfig'
import { monitorBaseUrl } from '@/api/config/base.js'
import { forceLogout } from '@/api/monitor/online.js'
import to from '@/utils/to'
import { online } from '@/views/pageName.js'

const proxy = inject('proxy')
const pageName = online
const requestBaseUrl = monitorBaseUrl
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const tableHideItems = ref([])
const headerButtons = ['refresh', 'columnDisplay', 'comSearch']

const searchConfig = getSearchConfig()
const searchConfigComputed = computed(() => {
  return getComputedConfig(searchConfig, {})
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

const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
}

const handleForceLogout = async (row) => {
  const [res] = await to(forceLogout(row.tokenId))
  if (res) {
    search()
    proxy.$modal.msgSuccess('强退成功')
  }
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
      :tableListener="tableListener"
      :tableSelected="tableSelected"
      :requestBaseUrl="requestBaseUrl"
      :headerButtons="headerButtons"
      :tableHideItems="tableHideItems"
      @onChangeShowColumn="onChangeShowColumn"
    >
      <template #loginTimeSlot="{ backData }">
        <span>{{ parseTime(backData.loginTime) }}</span>
      </template>
      <template #doSth="{ backData }">
        <a-popconfirm
          title="是否强退当前选中的用户？"
          ok-text="确认"
          cancel-text="取消"
          @confirm="handleForceLogout(backData)"
        >
          <a-button
            type="primary"
            size="small"
            v-hasPermi="['monitor:online:forceLogout']"
          >
            <template #icon>
              <SvgIcon size="14" iconClass="sign-out-alt" />
            </template>
            <span class="ml6">强退</span>
          </a-button>
        </a-popconfirm>
      </template>
    </PageContent>
  </div>
</template>

<style scoped lang="scss"></style>
