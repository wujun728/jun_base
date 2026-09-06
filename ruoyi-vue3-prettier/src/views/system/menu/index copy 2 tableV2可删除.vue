<script setup name="Menu" lang="jsx">
import getSearchConfig from './config/searchConfig.js'
import getContentConfig from './config/contentConfig.js'
import getDialogConfig from './config/dialogConfig.js'
import useDialog from '@/hooks/useDialog'
import getComputedConfig from '@/hooks/getPageConfig'
import IconSelector from '@/components/IconSelector/IconSelector.vue'
import { systemBaseUrl } from '@/api/config/base.js'
import { menu } from '@/views/pageName.js'
import { withDirectives } from 'vue'
const proxy = inject('proxy')
const { sys_normal_disable, sys_show_hide } = proxy.useDict(
  'sys_normal_disable',
  'sys_show_hide'
)
const pageName = menu
const requestBaseUrl = systemBaseUrl
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
// 控制页面排序字段
const descConfig = ref({})
// 点击保存会带上这里面的值（如果和要提交的表单键冲突那么会优先表单）
const otherInfo = ref({})
// 弹出层表单默认值
const defaultData = ref({
  menuType: 'M',
})
const tableData = ref([])
const piniaConfig = {
  listConfig: { listKey: 'data', countKey: 'total' },
  handleList: (list) => {
    const treeList = proxy.handleTree(list, 'menuId')
    const menu = { menuId: 0, menuName: '主类目', children: [] }
    menu.children = treeList
    tableData.value = [menu]
    return treeList
  },
}
// 弹出层要隐藏的formItem
const dialogHideItems = ref([])

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
const tableSelected = ref([])
const tableListener = {
  selectionChange: (selected) => {
    tableSelected.value = selected
  },
}
// 弹出成form配置
const dialogLisenter = {
  menuTypeChange: (value) => {
    changeDialogHide(value)
  },
}
const changeDialogHide = (value) => {
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
const editCallBack = async (item, type) => {
  changeDialogHide(item.menuType)
  isEditMore.value = type
}
// 是不是多选之后再点的编辑
const isEditMore = ref(false)
// 获取弹出层配置
const [dialogRef, infoInit, addClick, editBtnClick] = useDialog(
  addCallBack,
  editCallBack,
  '添加'
)
const search = () => {
  pageSearchRef.value?.search()
}

// 页面查询的前置函数
const beforeSend = (queryInfo) => {
  if (queryInfo.dateRange && Array.isArray(queryInfo.dateRange)) {
    const dateRange = queryInfo.dateRange
    queryInfo['params[beginTime]'] = dateRange[0]
    queryInfo['params[endTime]'] = dateRange[1]
    delete queryInfo.dateRange
  }
}
// table上面的按钮权限配置
const permission = {
  add: 'system:menu:add',
  edit: 'system:menu:edit',
  del: 'system:menu:remove',
}

const handleAdd = (row) => {
  addClick()
  nextTick(() => {
    dialogRef.value?.setFormData('parentId', row.menuId)
  })
}

const unFoldAll = () => {
  pageContentRef.value?.baseTableRef.unFoldAll()
}
const SelectionCell = ({ value, intermediate = false, onChange }) => {
  return (
    <ElCheckbox
      onChange={onChange}
      modelValue={value}
      indeterminate={intermediate}
    />
  )
}
const editClick = (row) => {
  pageContentRef.value?.editClick(row)
}
const deleteRow = (row) => {
  pageContentRef.value?.deleteRow(row)
}
const hasPermi = resolveDirective('hasPermi')
const addVnode = (rowData) => {
  return withDirectives(
    <ElButton
      class="order1"
      size="small"
      type="primary"
      onClick={() => handleAdd(rowData)}
    >
      <SvgIcon size="11" iconClass="plus" />
      <span class="ml6">添加</span>
    </ElButton>,
    [[hasPermi, [permission.add]]]
  )
}
const editVnode = (rowData) => {
  return withDirectives(
    <ElButton
      class="order1"
      size="small"
      type="primary"
      onClick={() => editClick(rowData)}
      v-hasPermi="['system:menu:edit']"
    >
      <SvgIcon size="10" iconClass="pencil"></SvgIcon>
      <span class="ml6">编辑</span>
    </ElButton>,
    [[hasPermi, [permission.eidt]]]
  )
}
const delVnode = () => {
  return withDirectives(
    <ElButton type="danger" size="small">
      <SvgIcon size="10" iconClass="trash"></SvgIcon>
      <span class="ml6">删除</span>
    </ElButton>,
    [[hasPermi, [permission.del]]]
  )
}
const todoCell = ({ rowData }) => {
  return (
    <div>
      {addVnode(rowData)}
      {editVnode(rowData)}
      <ElPopconfirm
        title="确定删除选中记录？"
        confirm-button-text="确认"
        cancel-button-text="取消"
        confirmButtonType="danger"
        hide-after={0}
        onConfirm={() => deleteRow(rowData)}
      >
        {{
          reference: () => delVnode(rowData),
        }}
      </ElPopconfirm>
    </div>
  )
}
const otherContentConfig = {
  // selectionCell: ({ rowData }) => {
  //   const onChange = (value) => (rowData.checked = value)
  //   return <SelectionCell value={rowData.checked} onChange={onChange} />
  // },
  // selectionHeader: () => {
  //   const _data = unref(tableData)
  //   const onChange = (value) =>
  //     (tableData.value = _data.map((row) => {
  //       row.checked = value
  //       return row
  //     }))
  //   const allSelected = _data.every((row) => row.checked)
  //   const containsChecked = _data.some((row) => row.checked)
  //   return (
  //     <SelectionCell
  //       value={allSelected}
  //       intermediate={containsChecked && !allSelected}
  //       onChange={onChange}
  //     />
  //   )
  // },
  iconCell: ({ rowData }) => {
    if (rowData.icon) {
      return (
        <SvgIcon
          iconClass={rowData.icon}
          size="16"
          color="var(--el-table-text-color)"
        />
      )
    } else {
      return ''
    }
  },
  todoCell,
}
const contentConfig = getContentConfig(otherContentConfig)
</script>
<template>
  <div class="default-main page">
    <PageSearch
      ref="pageSearchRef"
      :pageName="pageName"
      :searchConfig="searchConfigComputed"
    ></PageSearch>
    <PageContentV2
      ref="pageContentRef"
      :pageName="pageName"
      :contentConfig="contentConfig"
      :descConfig="descConfig"
      :tableListener="tableListener"
      :tableSelected="tableSelected"
      :permission="permission"
      :piniaConfig="piniaConfig"
      :requestBaseUrl="requestBaseUrl"
      @beforeSend="beforeSend"
      @addClick="addClick"
      @editBtnClick="editBtnClick"
    >
      <template #handleLeft>
        <el-button class="order16 ml12" type="info" @click="unFoldAll">
          展开/折叠
        </el-button>
      </template>
    </PageContentV2>
    <PageDialog
      ref="dialogRef"
      :width="getWidth(dialogWidth)"
      :pageName="pageName"
      :dialogConfig="dialogConfigComputed"
      :infoInit="infoInit"
      :search="search"
      :isEditMore="isEditMore"
      :otherInfo="otherInfo"
      :defaultData="defaultData"
      :requestBaseUrl="requestBaseUrl"
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
