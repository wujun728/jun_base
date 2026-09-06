<script setup name="Role">
import { ElNotification } from 'element-plus'
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig.js'
import getDialogConfig from './config/dialogConfig.js'
import useDialog from '@/hooks/useDialog'
import getComputedConfig from '@/hooks/getPageConfig'
import to from '@/utils/to'
import { changeRoleStatus, getRole } from '@/api/system/role'
import { roleMenuTreeselect, treeselect } from '@/api/system/menu'
import AssignDialog from './components/AssignDialog.vue'
import { systemBaseUrl } from '@/api/config/base.js'
import { role } from '@/views/pageName.js'
const pageId = useId()
const router = useRouter()
const proxy = inject('proxy')
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')
const pageName = role
const requestBaseUrl = systemBaseUrl
const pageSearchRef = ref(null)
const pageContentRef = ref(null)
const treeSelectInfo = ref([])
const menuExpand = ref(false)
const menuNodeAll = ref(false)
const menuCheckStrictly = ref(true)
const getTreeSelect = async () => {
  const [res] = await to(treeselect())
  treeSelectInfo.value = res.data ?? []
}
const dialogHideItems = ref([])
const tableHideItems = ref([])
const dictMap = {
  status: sys_normal_disable,
  menuIds: treeSelectInfo,
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

const dialogConfigComputed = computed(() => {
  const dialogConfig = getDialogConfig({
    checkStrictly: !menuCheckStrictly.value,
    defaultExpandAll: menuExpand.value,
  })
  dialogConfig.hideItems = dialogHideItems
  return getComputedConfig(dialogConfig, dictMap)
})

const addCallBack = () => {
  getTreeSelect()
}
const editCallBack = async (item, type) => {
  const roleId = item.roleId
  const [res] = await to(getRoleMenuTreeselect(roleId))
  if (res) {
    let checkedKeys = res.checkedKeys
    setTreeData(checkedKeys)
  }
  isEditMore.value = type
}
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

const editNext = (data) => {
  pageContentRef.value?.editClick(data, true)
}

const [dialogRef, infoInit, addClick, editBtnClick] = useDialog(
  addCallBack,
  editCallBack,
  '添加'
)

const dialogWidth = ref('550px')
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
const beforeSave = () => {
  dialogRef.value.setFormData('menuIds', getTreeData())
}

const permission = ref({
  add: 'system:role:add',
  edit: 'system:role:edit',
  del: 'system:role:remove',
})

const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy.download(
    'system/role/export',
    {
      ...searchData.value,
    },
    `role${new Date().getTime()}.xlsx`
  )
}

const handleStatusChange = async (row) => {
  let text = row.status === '0' ? '启用' : '停用'
  const [res, err] = await to(changeRoleStatus(row.roleId, row.status))
  if (res) {
    ElNotification({
      type: 'success',
      message: text + '成功',
    })
  }
  if (err) {
    row.status = row.status === '0' ? '1' : '0'
  }
}

/** 根据角色ID查询菜单树结构 */
const getRoleMenuTreeselect = async (roleId) => {
  const [res] = await to(roleMenuTreeselect(roleId))
  treeSelectInfo.value = res.menus
  return res
}

const setTreeData = (checkedKeys) => {
  nextTick(() => {
    checkedKeys.forEach((item) => {
      dialogRef.value?.formRef?.allRefs?.menuIds?.setChecked(item, true, false)
    })
  })
}
const getTreeData = () => {
  const treeRef = dialogRef.value?.formRef?.allRefs?.menuIds
  if (treeRef) {
    let checkedKeys = treeRef.getCheckedKeys()
    let halfCheckedKeys = treeRef.getHalfCheckedKeys()
    checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
    return checkedKeys
  } else {
    return []
  }
}

const handleAuthUser = (row) => {
  router.push('/system/role-auth/user/' + row.roleId)
}

const assignInfoInit = ref({})
const assignDialogVisible = ref(false)
const currentRoleId = ref('')
const handleDataScope = async (row) => {
  currentRoleId.value = row.roleId
  const [res] = await to(getRole(currentRoleId.value))
  if (res) {
    assignInfoInit.value = res.data
    assignDialogVisible.value = true
  }
}
const handleEditShow = (row) => {
  return row.roleId !== 1
}
const handleDeleteShow = (row) => {
  return row.roleId !== 1
}
const expandOrCollapseAll = (expand) => {
  const treeRef = dialogRef.value.formRef.allRefs.menuIds
  let treeList = treeSelectInfo.value
  for (let i = 0; i < treeList.length; i++) {
    treeRef.store.nodesMap[treeList[i].id].expanded = expand
  }
}
const handleCheckedTreeNodeAll = (value) => {
  const treeRef = dialogRef.value.formRef.allRefs.menuIds
  treeRef.setCheckedNodes(value ? treeSelectInfo.value : [])
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
      :requestBaseUrl="requestBaseUrl"
      :handleEditShow="handleEditShow"
      :handleDeleteShow="handleDeleteShow"
      @beforeSend="beforeSend"
      @addClick="addClick"
      @editBtnClick="editBtnClick"
      @onChangeShowColumn="onChangeShowColumn"
      @editMoreClick="editMoreClick"
    >
      <template #handleLeft>
        <el-button
          class="ml12 order16"
          type="warning"
          v-hasPermi="['system:user:export']"
          @click="handleExport"
        >
          <SvgIcon size="14" iconClass="download" />
          <span class="ml6">导出</span>
        </el-button>
      </template>
      <template #statusSlot="{ backData }">
        <el-switch
          v-if="backData.roleId !== 1"
          v-model="backData.status"
          active-value="0"
          inactive-value="1"
          @click="handleStatusChange(backData)"
        ></el-switch>
        <span v-else></span>
      </template>
      <template #deptSlot="{ backData }">
        <span> {{ backData.dept?.deptName }}</span>
      </template>
      <template #todoSlot="{ backData }">
        <el-button
          class="order6 ml12"
          size="small"
          type="primary"
          @click="handleDataScope(backData)"
          v-hasPermi="['system:role:edit']"
          v-if="backData.roleId !== 1"
        >
          <SvgIcon size="12" iconClass="random" />
          <span class="ml6">数据权限</span>
        </el-button>
        <el-button
          class="mt6 order11"
          size="small"
          type="primary"
          @click="handleAuthUser(backData)"
          v-hasPermi="['system:role:edit']"
          v-if="backData.roleId !== 1"
        >
          <SvgIcon size="12" iconClass="user" />
          <span class="ml6">分配用户</span>
        </el-button>
      </template>
    </PageContent>
    <PageDialog
      ref="dialogRef"
      :width="getWidth(dialogWidth)"
      :pageName="pageName"
      :dialogConfig="dialogConfigComputed"
      :infoInit="infoInit"
      :isEditMore="isEditMore"
      :search="search"
      :requestBaseUrl="requestBaseUrl"
      @editNext="editNext"
      @beforeSave="beforeSave"
    >
      <template #menuIdsBefore="{ backData }">
        <div>
          <span>{{ backData.label }}</span>
          <el-checkbox v-model="menuExpand" @change="expandOrCollapseAll">
            展开/折叠
          </el-checkbox>
          <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll">
            全选/全不选
          </el-checkbox>
          <el-checkbox v-model="menuCheckStrictly"> 父子联动 </el-checkbox>
        </div>
      </template>
    </PageDialog>
    <AssignDialog
      v-model="assignDialogVisible"
      :infoInit="assignInfoInit"
      :roleId="currentRoleId"
      @commitClick="search"
    ></AssignDialog>
  </div>
</template>

<style scoped lang="scss">
.page {
  :deep(.statusClass .el-radio-group) {
    width: 100%;
  }
  :deep(.menuIdsClass) {
    .el-tree {
      border: 1px solid var(--el-border-color);
      border-radius: 4px;
    }
  }
}
</style>
