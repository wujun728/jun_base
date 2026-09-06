<script setup name="Dept" lang="ts">
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig'
import getDialogConfig from './config/dialogConfig'
import useDialog from '@/hooks/useDialog.ts'
import getComputedConfig from '@/hooks/getPageConfig'
import { systemBaseUrl } from '@/api/config/base'
import { dept } from '@/views/pageName.ts'
import { proxy } from '@/utils/provide'

const { sys_normal_disable } = proxy.useDict('sys_normal_disable')
const pageName = dept
const requestBaseUrl = systemBaseUrl
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const treeSelectInfo = ref<anyObj[]>([])
const piniaConfig = {
  listConfig: { listKey: 'data', countKey: 'total' },
  handleList: (list: anyObj[]) => {
    treeSelectInfo.value = proxy.handleTree(list, 'deptId')
    return treeSelectInfo.value
  },
}
// 弹出层要隐藏的formItem
const dialogHideItems = ref<string[]>([])
// 表格要隐藏的列
const tableHideItems = ref<string[]>([])

const dictMap = {
  status: sys_normal_disable,
  parentId: treeSelectInfo,
}
// 搜索框配置
const searchConfig = getSearchConfig()
const searchConfigComputed = computed(() => {
  return getComputedConfig(searchConfig, dictMap)
})
// 列表配置
const tableSelected = ref<DeptItem[]>([])
const tableListener = {
  selectionChange: (selected: DeptItem[]) => {
    tableSelected.value = selected
  },
}
const contentConfig = getContentConfig()
const contentConfigComputed = computed(() => {
  contentConfig.hideItems = tableHideItems
  return contentConfig
})
// 弹出层form配置
const dialogWidth = ref('650px')
const dialogConfig = getDialogConfig()
const dialogConfigComputed = computed(() => {
  dialogConfig.hideItems = dialogHideItems
  return getComputedConfig(dialogConfig, dictMap)
})
// 点击添加的回调函数
const addCallBack = () => {
  dialogHideItems.value = []
}
// 点击编辑的回调函数
const editCallBack = async (item: DeptItem, type: any) => {
  if (item.parentId === 0) {
    dialogHideItems.value = ['parentId']
  } else {
    dialogHideItems.value = []
  }
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
const editNext = (data: DeptItem) => {
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
  add: 'system:dept:add',
  edit: 'system:dept:edit',
  del: 'system:dept:remove',
})
// 控制table每一列的显示隐藏
const onChangeShowColumn = (filterArr: string[]) => {
  tableHideItems.value = filterArr
}
const handleAdd = (row: DeptItem) => {
  addClick()
  nextTick(() => {
    if (row.parentId === 0) {
      dialogRef.value?.setFormData('parentId', 100)
    } else {
      console.log(row)
      dialogRef.value?.setFormData('parentId', row.deptId)
    }
  })
}

const foldAll = ref(true)
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
          :type="foldAll ? 'warning' : 'info'"
          @click="unFoldAll"
        >
          <span v-show="!foldAll">展开所有</span>
          <span v-show="foldAll">收缩所有</span>
        </el-button>
      </template>
      <template #statusSlot="{ backData }">
        <el-tag :type="backData.status == 0 ? 'success' : 'danger'">
          {{ backData.status == 0 ? '启用' : '禁用' }}
        </el-tag>
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
    </PageContent>
    <PageDialog
      ref="dialogRef"
      :width="getWidth(dialogWidth)"
      :pageName="pageName"
      :dialogConfig="dialogConfigComputed"
      :infoInit="infoInit"
      :search="search"
      :isEditMore="isEditMore"
      :requestBaseUrl="requestBaseUrl"
      @editNext="editNext"
      @beforeSave="beforeSave"
    >
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
