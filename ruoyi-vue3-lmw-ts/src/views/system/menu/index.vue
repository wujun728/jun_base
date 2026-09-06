<script setup name="Menu" lang="ts">
import getSearchConfig from './config/searchConfig.ts'
import getContentConfig from './config/contentConfig.ts'
import getDialogConfig from './config/dialogConfig.ts'
import useDialog from '@/hooks/useDialog'
import getComputedConfig from '@/hooks/getPageConfig'
import IconSelector from '@/components/IconSelector/IconSelector.vue'
import { systemBaseUrl } from '@/api/config/base'
import { menu } from '@/views/pageName'
import { proxy } from '@/utils/provide'

const { sys_normal_disable, sys_show_hide } = proxy.useDict(
  'sys_normal_disable',
  'sys_show_hide'
)
const pageName = menu
const requestBaseUrl = systemBaseUrl
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
// 弹出层表单默认值
const defaultData = ref({
  menuType: 'M',
})
const tableData = ref<anyObj[]>([])
const piniaConfig = {
  listConfig: { listKey: 'data', countKey: 'total' },
  handleList: (list: anyObj[]) => {
    const treeList = proxy.handleTree(list, 'menuId')
    tableData.value = [{ menuId: 0, menuName: '主类目', children: treeList }]
    return treeList
  },
}
// 弹出层要隐藏的formItem
const dialogHideItems = ref<string[]>([])
// 表格要隐藏的列
const tableHideItems = ref<string[]>([])

const dictMap = {
  status: sys_normal_disable,
  parentId: tableData,
  visible: sys_show_hide,
}
// 搜索框配置
const searchConfig = getSearchConfig()
const searchConfigComputed = computed(() => {
  return getComputedConfig(searchConfig, dictMap)
})
// 列表配置
const tableSelected = ref<MenuItem[]>([])
const tableListener = {
  selectionChange: (selected: MenuItem[]) => {
    tableSelected.value = selected
  },
}
const contentConfig = getContentConfig()
const contentConfigComputed = computed(() => {
  contentConfig.hideItems = tableHideItems
  return contentConfig
})
// 弹出层form配置
const dialogLisenter = {
  menuTypeChange: (value: string) => {
    changeDialogHide(value)
  },
}
const changeDialogHide = (value: string) => {
  dialogHideItems.value = []
  if (value === 'M') {
    dialogHideItems.value = ['component', 'query', 'isCache', 'perms']
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
  }
}
const dialogWidth = ref('700px')
const dialogConfig = getDialogConfig(dialogLisenter)
const dialogConfigComputed = computed(() => {
  dialogConfig.hideItems = dialogHideItems
  return getComputedConfig(dialogConfig, dictMap)
})
// 点击添加的回调函数
const addCallBack = () => {
  changeDialogHide(defaultData.value.menuType)
}
// 点击编辑的回调函数
const editCallBack = async (item: MenuItem, type: any) => {
  changeDialogHide(item.menuType)
  isEditMore.value = type
}
// 是不是多选之后再点的编辑
const isEditMore = ref(false)
const editMoreClick = () => {
  if (tableSelected.value.length > 0) {
    const data = tableSelected.value[0]
    pageContentRef.value?.editClick(data, true)
    nextTick(() => {
      const newArray = tableSelected.value.slice(1)
      dialogRef.value?.changeSelected(newArray)
    })
  }
}
// 编辑下一个
const editNext = (data: MenuItem) => {
  pageContentRef.value?.editClick(data, true)
}
// 获取弹出层配置
const { dialogRef, infoInit, addClick, editBtnClick } = useDialog(
  addCallBack,
  editCallBack,
  '添加'
)
const search = () => {
  pageSearchRef.value?.search()
}

// 页面查询的前置函数
const beforeSend = (queryInfo: anyObj) => {
  if (queryInfo.dateRange && Array.isArray(queryInfo.dateRange)) {
    const dateRange = queryInfo.dateRange
    queryInfo['params[beginTime]'] = dateRange[0]
    queryInfo['params[endTime]'] = dateRange[1]
    delete queryInfo.dateRange
  }
}
// 弹出层保存请求发送之前回调函数
const beforeSave = () => {}
// table上面的按钮权限配置
const permission = ref({
  add: 'system:menu:add',
  edit: 'system:menu:edit',
  del: 'system:menu:remove',
})

// 控制table每一列的显示隐藏
const onChangeShowColumn = (filterArr: string[]) => {
  tableHideItems.value = filterArr
}

const handleAdd = (row: MenuItem) => {
  addClick()
  nextTick(() => {
    dialogRef.value?.setFormData('parentId', row.menuId)
  })
}
const foldAll = ref(false)
const unFoldAll = () => {
  foldAll.value = !foldAll.value
  pageContentRef.value?.baseTableRef?.unFoldAll(foldAll.value)
}
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
      :piniaConfig="piniaConfig"
      :requestBaseUrl="requestBaseUrl"
      @beforeSend="beforeSend"
      @addClick="addClick"
      @editBtnClick="editBtnClick"
      @onChangeShowColumn="onChangeShowColumn"
      @editMoreClick="editMoreClick"
    >
      <template #handleLeft>
        <el-button
          class="order16 ml12"
          @click="unFoldAll"
          :type="foldAll ? 'warning' : 'info'"
        >
          <span v-show="!foldAll">展开所有</span>
          <span v-show="foldAll">收缩所有</span>
        </el-button>
      </template>
      <template #todoSlot="{ backData }">
        <el-button
          class="order1"
          size="small"
          type="primary"
          @click="handleAdd(backData)"
          v-hasPermi="permission.add"
        >
          <SvgIcon size="12" iconClass="plus" />
          <span class="ml6">添加</span>
        </el-button>
      </template>
      <template #iconSlot="{ backData }">
        <SvgIcon v-if="backData.icon" :iconClass="backData.icon" :size="16" />
      </template>
    </PageContent>
    <PageDialog
      ref="dialogRef"
      :width="getWidth(dialogWidth)"
      :pageName="pageName"
      :dialogConfig="dialogConfigComputed"
      :infoInit="infoInit"
      :search="search"
      :isEditMore="isEditMore"
      :defaultData="defaultData"
      :requestBaseUrl="requestBaseUrl"
      @editNext="editNext"
      @beforeSave="beforeSave"
    >
      <template #iconCustom="{ backData }">
        <IconSelector v-model="backData.formData.icon"></IconSelector>
      </template>
    </PageDialog>
  </div>
</template>

<style scoped lang="scss">
.page {
  :deep(.page-dialog .el-radio) {
    margin-right: 20px;
  }
  :deep(.statusClass .el-radio-group) {
    width: 100%;
  }
}
</style>
