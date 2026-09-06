<script setup name="Role">
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

const router = useRouter()
const proxy = inject('proxy')
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')
const pageName = role
const requestBaseUrl = systemBaseUrl
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const treeSelectInfo = ref([])
const menuExpand = ref(false)
const menuNodeAll = ref(false)
const menuCheckStrictly = ref(true)
const menuExpandedKeys = ref([])

const getTreeSelect = async () => {
  const [res] = await to(treeselect())
  treeSelectInfo.value = res?.data ?? []
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
  const menuIdsItem = dialogConfig.formItems.find(
    (item) => item.field === 'menuIds'
  )
  if (menuIdsItem) {
    menuIdsItem.config = {
      ...menuIdsItem.config,
      expandedKeys: menuExpandedKeys.value,
    }
    menuIdsItem.eventFunction = {
      'update:expandedKeys': (keys) => {
        menuExpandedKeys.value = keys
      },
    }
  }
  return getComputedConfig(dialogConfig, dictMap)
})

const collectTreeKeys = (nodes, keys = []) => {
  nodes.forEach((node) => {
    keys.push(node.id)
    if (node.children?.length) {
      collectTreeKeys(node.children, keys)
    }
  })
  return keys
}

const collectExpandableKeys = (nodes, keys = []) => {
  nodes.forEach((node) => {
    if (node.children?.length) {
      keys.push(node.id)
      collectExpandableKeys(node.children, keys)
    }
  })
  return keys
}

const normalizeCheckedKeys = (checkedKeys) => {
  if (menuCheckStrictly.value) {
    return {
      checked: checkedKeys,
      halfChecked: [],
    }
  }
  return checkedKeys
}

const addCallBack = () => {
  getTreeSelect()
  menuExpand.value = false
  menuNodeAll.value = false
  menuCheckStrictly.value = true
  menuExpandedKeys.value = []
}

const editCallBack = async (item) => {
  const [res] = await to(getRoleMenuTreeselect(item.roleId))
  if (res) {
    setTreeData(res.checkedKeys)
  }
}

const [dialogRef, infoInit, addClick, editBtnClick] = useDialog(
  addCallBack,
  editCallBack,
  '添加'
)

const dialogWidth = ref('550px')
const searchData = computed(() => pageContentRef.value?.finalSearchData)

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

const handleExport = () => {
  proxy.download(
    'system/role/export',
    {
      ...searchData.value,
    },
    `role_${new Date().getTime()}.xlsx`
  )
}

const handleStatusChange = async (row) => {
  row.statusLoading = true
  const text = row.status === '0' ? '启用' : '停用'
  const [res] = await to(changeRoleStatus(row.roleId, row.status))
  if (res) {
    proxy.$modal.msgSuccess(`${text}成功`)
  } else {
    row.status = row.status === '0' ? '1' : '0'
  }
  row.statusLoading = false
}

const getRoleMenuTreeselect = async (roleId) => {
  const [res] = await to(roleMenuTreeselect(roleId))
  if (res) {
    treeSelectInfo.value = res.menus
  }
  return res
}

const setTreeData = (checkedKeys) => {
  nextTick(() => {
    dialogRef.value?.setFormData('menuIds', normalizeCheckedKeys(checkedKeys))
  })
}

const getTreeData = () => {
  const menuIds = dialogRef.value?.formData?.menuIds
  if (!menuIds) {
    return []
  }
  if (Array.isArray(menuIds)) {
    return menuIds
  }
  const { checked = [], halfChecked = [] } = menuIds
  return [...halfChecked, ...checked]
}

const handleAuthUser = (row) => {
  router.push(`/system/role-auth/user/${row.roleId}`)
}

const assignInfoInit = ref({})
const assignDialogVisible = ref(false)
const currentRoleId = ref('')

const handleDataScope = async (row) => {
  currentRoleId.value = row.roleId
  const [res] = await to(getRole(row.roleId))
  if (res) {
    assignInfoInit.value = res.data
    assignDialogVisible.value = true
  }
}

const handleEditShow = (row) => row.roleId !== 1
const handleDeleteShow = (row) => row.roleId !== 1

watch(menuExpand, (expand) => {
  menuExpandedKeys.value = expand
    ? collectExpandableKeys(treeSelectInfo.value)
    : []
})

watch(menuNodeAll, (checked) => {
  if (!dialogRef.value) {
    return
  }
  if (!checked) {
    dialogRef.value.setFormData(
      'menuIds',
      menuCheckStrictly.value ? { checked: [], halfChecked: [] } : []
    )
    return
  }
  dialogRef.value.setFormData(
    'menuIds',
    normalizeCheckedKeys(collectTreeKeys(treeSelectInfo.value))
  )
})
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
      :handleEditShow="handleEditShow"
      :handleDeleteShow="handleDeleteShow"
      :tableHideItems="tableHideItems"
      @beforeSend="beforeSend"
      @addClick="addClick"
      @editBtnClick="editBtnClick"
      @onChangeShowColumn="onChangeShowColumn"
    >
      <template #handleLeft>
        <a-button
          class="order16"
          v-hasPermi="['system:role:export']"
          type="dashed"
          @click="handleExport"
        >
          <template #icon>
            <SvgIcon size="14" iconClass="export" />
          </template>
          <span class="ml6">导出</span>
        </a-button>
      </template>
      <template #statusSlot="{ backData }">
        <a-switch
          v-if="backData.roleId !== 1"
          v-model:checked="backData.status"
          checked-value="0"
          un-checked-value="1"
          :loading="backData.statusLoading"
          @change="() => handleStatusChange(backData)"
        />
        <DictTag
          v-else
          :value="backData.status"
          :options="sys_normal_disable"
        />
      </template>
      <template #todoSlot="{ backData }">
        <a-button
          v-if="backData.roleId !== 1"
          class="order6 ml12"
          size="small"
          type="primary"
          v-hasPermi="['system:role:edit']"
          @click="handleDataScope(backData)"
        >
          <template #icon>
            <SvgIcon size="12" iconClass="random" />
          </template>
          <span class="ml6">数据权限</span>
        </a-button>
        <a-button
          v-if="backData.roleId !== 1"
          class="order11 ml12"
          size="small"
          type="primary"
          v-hasPermi="['system:role:edit']"
          @click="handleAuthUser(backData)"
        >
          <template #icon>
            <SvgIcon size="12" iconClass="user" />
          </template>
          <span class="ml6">分配用户</span>
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
      @beforeSave="beforeSave"
    >
      <template #menuIdsHeader="{ backData }">
        <div class="menu-tree-toolbar">
          <div class="customLabel treeTitle">联动方式：</div>
          <a-checkbox v-model:checked="menuExpand">展开/折叠</a-checkbox>
          <a-checkbox v-model:checked="menuNodeAll">全选/全不选</a-checkbox>
          <a-checkbox v-model:checked="menuCheckStrictly">父子联动</a-checkbox>
        </div>
      </template>
    </PageDialog>
    <AssignDialog
      v-model="assignDialogVisible"
      :infoInit="assignInfoInit"
      :roleId="currentRoleId"
      @commitClick="search"
    />
  </div>
</template>

<style scoped lang="scss">
.page {
  :deep(.statusClass .ant-radio-group) {
    width: 100%;
    justify-content: space-between;

    .ant-radio-wrapper {
      margin-right: 0;
    }
  }

  :deep(.menuIdsClass) {
    .ant-tree {
      border: 1px solid var(--ba-border-color);
      border-radius: 4px;
      padding: 8px;
    }
  }
}

.menu-tree-toolbar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
  height: 52px;
  .treeTitle {
    text-align: right;
    width: 80px;
  }
}
</style>
