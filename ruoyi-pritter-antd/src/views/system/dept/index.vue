<script setup name="Dept">
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig.js'
import getDialogConfig from './config/dialogConfig.js'
import useDialog from '@/hooks/useDialog'
import getComputedConfig from '@/hooks/getPageConfig'
import { systemBaseUrl } from '@/api/config/base.js'
import { dept } from '@/views/pageName.js'

const proxy = inject('proxy')
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')
const pageName = dept
const requestBaseUrl = systemBaseUrl
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const treeSelectInfo = ref([])
const dialogHideItems = ref([])
const tableHideItems = ref([])

const piniaConfig = {
  listConfig: { listKey: 'data', countKey: 'total' },
  handleList: (list) => {
    treeSelectInfo.value = proxy.handleTree(list, 'deptId')
    return treeSelectInfo.value
  },
}

const dictMap = {
  status: sys_normal_disable,
  parentId: treeSelectInfo,
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
  dialogHideItems.value = []
}

const editCallBack = (item) => {
  dialogHideItems.value = item.parentId === 0 ? ['parentId'] : []
}

const [dialogRef, infoInit, addClick, editBtnClick] = useDialog(
  addCallBack,
  editCallBack,
  '添加'
)

const dialogWidth = ref('650px')

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
  add: 'system:dept:add',
  edit: 'system:dept:edit',
  del: 'system:dept:remove',
})

const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
}

const handleAdd = (row) => {
  addClick()
  nextTick(() => {
    dialogRef.value?.setFormData(
      'parentId',
      row.parentId === 0 ? 100 : row.deptId
    )
  })
}

const foldAll = ref(true)
const unFoldAll = () => {
  foldAll.value = !foldAll.value
  pageContentRef.value?.baseTableRef.unFoldAll(foldAll.value)
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
      :piniaConfig="piniaConfig"
      :requestBaseUrl="requestBaseUrl"
      :tableHideItems="tableHideItems"
      @beforeSend="beforeSend"
      @addClick="addClick"
      @editBtnClick="editBtnClick"
      @onChangeShowColumn="onChangeShowColumn"
    >
      <template #handleLeft>
        <a-button class="order16" :type="foldAll ? 'default' : 'dashed'" @click="unFoldAll">
          <span v-if="!foldAll">展开所有</span>
          <span v-else>收缩所有</span>
        </a-button>
      </template>
      <template #statusSlot="{ backData }">
        <DictTag :options="sys_normal_disable" :value="backData.status" />
      </template>
      <template #todoSlot="{ backData }">
        <a-button
          class="order1"
          size="small"
          type="primary"
          v-hasPermi="[permission.add]"
          @click="handleAdd(backData)"
        >
          <template #icon>
            <SvgIcon size="12" iconClass="plus" />
          </template>
          <span class="ml6">添加</span>
        </a-button>
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
