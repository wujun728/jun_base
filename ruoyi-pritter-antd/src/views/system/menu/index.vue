<script setup name="Menu">
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig.js'
import getDialogConfig from './config/dialogConfig.js'
import useDialog from '@/hooks/useDialog'
import getComputedConfig from '@/hooks/getPageConfig'
import IconSelector from '@/components/IconSelector/IconSelector.vue'
import { systemBaseUrl } from '@/api/config/base.js'
import { menu } from '@/views/pageName.js'

const proxy = inject('proxy')
const { sys_normal_disable, sys_show_hide } = proxy.useDict(
  'sys_normal_disable',
  'sys_show_hide'
)
const pageName = menu
const requestBaseUrl = systemBaseUrl
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const defaultData = ref({
  menuType: 'M',
  isFrame: '1',
  isCache: '0',
  visible: '0',
  status: '0',
})
const tableData = ref([])
const dialogHideItems = ref([])
const tableHideItems = ref([])

const piniaConfig = {
  listConfig: { listKey: 'data', countKey: 'total' },
  handleList: (list) => {
    const treeList = proxy.handleTree(list, 'menuId')
    const rootMenu = { menuId: 0, menuName: '主类目', children: [] }
    rootMenu.children = treeList
    tableData.value = [rootMenu]
    return treeList
  },
}

const dictMap = {
  status: sys_normal_disable,
  parentId: tableData,
  visible: sys_show_hide,
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

const changeDialogHide = (value) => {
  if (value === 'M') {
    dialogHideItems.value = ['component', 'query', 'isCache', 'perms']
    return
  }
  if (value === 'F') {
    dialogHideItems.value = [
      'icon',
      'isFrame',
      'path',
      'component',
      'query',
      'isCache',
      'visible',
    ]
    return
  }
  dialogHideItems.value = []
}

const dialogListener = {
  menuTypeChange: (e) => {
    changeDialogHide(e?.target?.value ?? e)
  },
}

const dialogWidth = ref('700px')
const dialogConfig = getDialogConfig(dialogListener)
const dialogConfigComputed = computed(() => {
  dialogConfig.hideItems = dialogHideItems
  return getComputedConfig(dialogConfig, dictMap)
})

const addCallBack = () => {
  changeDialogHide(defaultData.value.menuType)
}

const editCallBack = (item) => {
  changeDialogHide(item.menuType)
}

const [dialogRef, infoInit, addClick, editBtnClick] = useDialog(
  addCallBack,
  editCallBack,
  '添加'
)

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
  add: 'system:menu:add',
  edit: 'system:menu:edit',
  del: 'system:menu:remove',
})

const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
}

const handleAdd = (row) => {
  addClick()
  nextTick(() => {
    dialogRef.value?.setFormData('parentId', row.menuId)
  })
}

const foldAll = ref(false)
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
        <a-button
          class="order16"
          :type="foldAll ? 'default' : 'dashed'"
          @click="unFoldAll"
        >
          <span v-if="!foldAll">展开所有</span>
          <span v-else>收缩所有</span>
        </a-button>
      </template>
      <template #todoSlot="{ backData }">
        <a-button
          class="order1"
          size="small"
          type="primary"
          v-hasPermi="['system:menu:add']"
          @click="handleAdd(backData)"
        >
          <template #icon>
            <SvgIcon size="12" iconClass="plus" />
          </template>
          <span class="ml6">添加</span>
        </a-button>
      </template>
      <template #iconSlot="{ backData }">
        <SvgIcon v-if="backData.icon" :icon-class="backData.icon" :size="16" />
      </template>
    </PageContent>
    <PageDialog
      ref="dialogRef"
      :width="getWidth(dialogWidth)"
      :pageName="pageName"
      :dialogConfig="dialogConfigComputed"
      :infoInit="infoInit"
      :defaultData="defaultData"
      :search="search"
      :requestBaseUrl="requestBaseUrl"
    >
      <template #iconCustom="{ backData }">
        <IconSelector v-model="backData.formData.icon" />
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
