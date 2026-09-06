<script setup name="Notice">
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig.js'
import getDialogConfig from './config/dialogConfig.js'
import useDialog from '@/hooks/useDialog'
import getComputedConfig from '@/hooks/getPageConfig'
import Editor from '@/components/Editor/index.vue'
import { systemBaseUrl } from '@/api/config/base.js'
import { notice } from '@/views/pageName.js'

const proxy = inject('proxy')
const { sys_notice_status, sys_notice_type } = proxy.useDict(
  'sys_notice_status',
  'sys_notice_type'
)
const pageName = notice
const requestBaseUrl = systemBaseUrl
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const dialogHideItems = ref([])
const tableHideItems = ref([])

const dictMap = {
  status: sys_notice_status,
  noticeType: sys_notice_type,
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

const dialogWidth = ref('700px')

const search = () => {
  pageSearchRef.value?.search()
}

const permission = ref({
  add: 'system:notice:add',
  edit: 'system:notice:edit',
  del: 'system:notice:remove',
})

const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
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
      @addClick="addClick"
      @editBtnClick="editBtnClick"
      @onChangeShowColumn="onChangeShowColumn"
    >
      <template #noticeTypeSlot="{ backData }">
        <DictTag :options="sys_notice_type" :value="backData.noticeType" />
      </template>
      <template #statusSlot="{ backData }">
        <DictTag :options="sys_notice_status" :value="backData.status" />
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
    >
      <template #noticeContentCustom="{ backData }">
        <Editor v-model="backData.formData.noticeContent" :min-height="192" />
      </template>
    </PageDialog>
  </div>
</template>

<style scoped lang="scss">
.page {
  :deep(.statusClass .ant-radio-group) {
    width: 100%;
    flex-wrap: wrap;
    gap: 8px;
  }
}
</style>
