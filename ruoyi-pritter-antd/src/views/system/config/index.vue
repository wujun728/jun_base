<script setup name="Config">
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig.js'
import getDialogConfig from './config/dialogConfig.js'
import useDialog from '@/hooks/useDialog'
import getComputedConfig from '@/hooks/getPageConfig'
import { systemBaseUrl } from '@/api/config/base.js'
import { config } from '@/views/pageName.js'

const proxy = inject('proxy')
const { sys_yes_no } = proxy.useDict('sys_yes_no')
const pageName = config
const requestBaseUrl = systemBaseUrl
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const dialogHideItems = ref([])
const tableHideItems = ref([])
const dictMap = {
  configType: sys_yes_no,
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
const dialogConfigComputed = computed(() => {
  dialogConfig.hideItems = dialogHideItems
  return getComputedConfig(dialogConfig, dictMap)
})

const addCallBack = () => {
  dialogHideItems.value.length = 0
}

const editCallBack = () => {}

const [dialogRef, infoInit, addClick, editBtnClick] = useDialog(
  addCallBack,
  editCallBack,
  '添加'
)

const dialogWidth = ref('600px')
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
  add: 'system:config:add',
  edit: 'system:config:edit',
  del: 'system:config:remove',
})

const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
}

const handleExport = () => {
  proxy.download(
    'system/config/export',
    {
      ...searchData.value,
    },
    `config_${new Date().getTime()}.xlsx`
  )
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
      :tableHideItems="tableHideItems"
      @beforeSend="beforeSend"
      @addClick="addClick"
      @editBtnClick="editBtnClick"
      @onChangeShowColumn="onChangeShowColumn"
    >
      <template #handleLeft>
        <a-button
          class="order16"
          type="dashed"
          v-hasPermi="['system:config:export']"
          @click="handleExport"
        >
          <template #icon>
            <SvgIcon size="14" iconClass="export" />
          </template>
          <span class="ml6">导出</span>
        </a-button>
      </template>
      <template #configTypeSlot="{ backData }">
        <DictTag :options="sys_yes_no" :value="backData.configType" />
      </template>
    </PageContent>
    <PageDialog
      ref="dialogRef"
      :width="getWidth(dialogWidth)"
      :pageName="pageName"
      :dialogConfig="dialogConfigComputed"
      :infoInit="infoInit"
      :search="search"
      :requestBaseUrl="requestBaseUrl"
    />
  </div>
</template>

<style scoped lang="scss"></style>
