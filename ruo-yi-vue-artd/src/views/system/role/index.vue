<template>
  <div>
    <ArtCrud
      :config="config"
      ref="crudRef"
      v-model:search="queryParams"
      v-model:edit-form="form"
      @add-row="handleAdd"
      @edit-row="handleUpdate"
      @selection-change="handleSelectionChange">
      <template #toolbar>
        <ElButton
          type="success"
          plain
          :icon="Edit"
          :disabled="single"
          @click="() => handleUpdate()"
          v-auth="'system:role:edit'">
          修改
        </ElButton>
        <ElButton
          type="danger"
          plain
          :icon="Delete"
          :disabled="multiple"
          @click="() => handleDelete()"
          v-auth="'system:role:remove'">
          删除
        </ElButton>
        <ElButton
          type="warning"
          plain
          :icon="Download"
          @click="() => handleExport()"
          v-auth="'system:role:export'">
          导出
        </ElButton>
      </template>
      <template #col-statusSlot="{ row }">
        <el-switch
          v-model="row.status"
          active-value="0"
          inactive-value="1"
          @change="handleStatusChange(row)"></el-switch>
      </template>
      <template #col-operationTemplate="{ row }">
        <ArtButtonTable
          v-if="row.roleId !== 1"
          v-auth="'system:role:edit'"
          tooltip="数据权限"
          icon="ri:checkbox-circle-line"
          iconClass="bg-warning/12 text-warning"
          @click="handleDataScope(row)">
          数据权限
        </ArtButtonTable>
        <ArtButtonTable
          v-if="row.roleId !== 1"
          v-auth="'system:role:edit'"
          tooltip="分配用户"
          icon="ri:user-3-line"
          iconClass="bg-amber-100 text-amber-900"
          @click="handleAuthUser(row)">
          分配用户
        </ArtButtonTable>
      </template>

      <template #form-menuIdsSlot="{ modelValue }">
        <el-checkbox
          v-model="menuExpand"
          @change="(value) => handleCheckedTreeExpand(value, 'menu')">
          展开/折叠
        </el-checkbox>
        <el-checkbox
          v-model="menuNodeAll"
          @change="(value) => handleCheckedTreeNodeAll(value, 'menu')">
          全选/全不选
        </el-checkbox>
        <el-checkbox
          v-model="form.menuCheckStrictly"
          @change="(value) => handleCheckedTreeConnect(value, 'menu')">
          父子联动
        </el-checkbox>
        <el-tree
          class="tree-border"
          :data="menuOptions"
          show-checkbox
          ref="menuRef"
          node-key="id"
          :check-strictly="!form.menuCheckStrictly"
          empty-text="加载中，请稍候"
          :props="{ label: 'label', children: 'children' }"></el-tree>
      </template>
    </ArtCrud>

    <DataScopeDialog ref="dataScopeDialogRef" @success="refreshTable" />
    <AuthUser ref="authUserRef" />
  </div>
</template>

<script setup lang="ts">
import type { CheckboxValueType, ElTree } from 'element-plus'

import { QuestionFilled, Edit, Delete, Download } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'

import ArtCrud, {
  ArtCrudConfig
} from '@/components/business/art-crud/index.vue'
import { useAuth } from '@/hooks/core/useAuth'
import { useDict } from '@/hooks/core/useDict'
import { RoleMenuTreeselectResult, TreeSelect } from '@/types/api'
import { SysRole } from '@/types/api/system/role'
import download from '@/utils/http/download'
import { addDateRange } from '@/utils/sys/ruoyi'

import {
  addRole,
  changeRoleStatus,
  delRole,
  getRole,
  listRole,
  menuTreeselect,
  roleMenuTreeselect,
  updateRole
} from './api'
import AuthUser from './components/authUser.vue'
import DataScopeDialog from './components/dataScopeDialog.vue'

defineOptions({ name: 'Role' })

const { sys_normal_disable } = useDict('sys_normal_disable')
const { hasAuth } = useAuth()

const crudRef = useTemplateRef<InstanceType<typeof ArtCrud>>('crudRef')
const menuRef = useTemplateRef<InstanceType<typeof ElTree>>('menuRef')
const dataScopeDialogRef =
  useTemplateRef<InstanceType<typeof DataScopeDialog>>('dataScopeDialogRef')
const authUserRef = useTemplateRef<InstanceType<typeof AuthUser>>('authUserRef')

const ids = ref<number[]>([])
const single = ref<boolean>(true)
const multiple = ref<boolean>(true)

const menuExpand = ref<boolean>(false)
const menuNodeAll = ref<boolean>(false)
const menuOptions = ref<TreeSelect[]>([])

const queryParams = ref({
  roleName: '',
  roleKey: '',
  status: '',
  createTime: ''
})
const form = ref<SysRole>({
  roleId: undefined,
  roleName: undefined,
  roleKey: undefined,
  roleSort: 0,
  status: '0',
  menuIds: [],
  deptIds: [],
  menuCheckStrictly: true,
  deptCheckStrictly: true,
  remark: undefined
})

/** 新增按钮操作 */
async function handleAdd() {
  getMenuTreeselect()
}

/** 修改按钮操作 */
async function handleUpdate(row?: SysRole) {
  // reset()
  const roleId = row?.roleId || ids.value[0]
  if (roleId === 1) {
    ElMessage.error('超级管理员角色不能修改')
    return
  }
  const roleMenu = getRoleMenuTreeselect(roleId)
  const response = await getRole(roleId)
  form.value = response
  form.value.roleSort = Number(form.value.roleSort)
  nextTick(() => {
    roleMenu.then((res: RoleMenuTreeselectResult) => {
      const checkedKeys = res.checkedKeys
      checkedKeys.forEach((v) => {
        nextTick(() => {
          menuRef.value?.setChecked(v, true, false)
        })
      })

      if (crudRef.value) {
        crudRef.value.openEdit(form.value)
      }
    })
  })
}

/** 删除按钮操作 */
function handleDelete(row?: SysRole) {
  const roleIds = row?.roleId || ids.value
  ElMessageBox.confirm('是否确认删除角色编号为"' + roleIds + '"的数据项?')
    .then(function () {
      return delRole(roleIds)
    })
    .then(() => {
      refreshTable()
      ElMessage.success('删除成功')
    })
    .catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  download(
    'system/role/export',
    {
      ...queryParams.value
    },
    `role_${new Date().getTime()}.xlsx`
  )
}

/** 分配数据权限操作 */
function handleDataScope(row: SysRole) {
  dataScopeDialogRef.value?.open(row)
}

/** 分配用户 */
function handleAuthUser(row: SysRole) {
  authUserRef.value?.open(row.roleId)
}

/** 刷新表格 */
function refreshTable() {
  if (crudRef.value) {
    crudRef.value.handleRefresh()
  }
}

/** 根据角色ID查询菜单树结构 */
async function getRoleMenuTreeselect(roleId: number) {
  const res = await roleMenuTreeselect(roleId)
  menuOptions.value = res.menus
  return res
}

/** 查询菜单树结构 */
async function getMenuTreeselect() {
  const res = await menuTreeselect()
  menuOptions.value = res
}

/** 树权限（展开/折叠）*/
function handleCheckedTreeExpand(value: CheckboxValueType, type: string) {
  if (type == 'menu') {
    let treeList = menuOptions.value
    for (let i = 0; i < treeList.length; i++) {
      if (menuRef.value) {
        menuRef.value.store.nodesMap[treeList[i].id as number].expanded =
          Boolean(value)
      }
    }
  }
}

/** 树权限（全选/全不选） */
function handleCheckedTreeNodeAll(value: CheckboxValueType, type: string) {
  if (type == 'menu') {
    if (menuRef.value) {
      let keys = (value as boolean) ? getAllKeys(menuOptions.value) : []
      menuRef.value.setCheckedKeys(keys)
    }
  }
}

/** 获取所有子节点的id */
function getAllKeys(nodes: any[]): number[] {
  const keys: number[] = []
  const loop = (list: any[]) => {
    list.forEach((item) => {
      keys.push(item.id)
      if (item.children?.length) {
        loop(item.children)
      }
    })
  }
  loop(nodes)
  return keys
}

/** 树权限（父子联动） */
function handleCheckedTreeConnect(value: CheckboxValueType, type: string) {
  if (type == 'menu') {
    form.value.menuCheckStrictly = Boolean(value)
  }
}

/** 所有菜单节点数据 */
function getMenuAllCheckedKeys(): number[] {
  if (!menuRef.value) {
    return []
  }
  // 目前被选中的菜单节点
  let checkedKeys = menuRef.value.getCheckedKeys()
  // 半选中的菜单节点
  let halfCheckedKeys = menuRef.value.getHalfCheckedKeys()
  checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
  return checkedKeys as number[]
}

/** 选择条数  */
function handleSelectionChange(selection: SysRole[]) {
  ids.value = selection.map((item) => item.roleId!)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 角色状态修改 */
function handleStatusChange(row: SysRole) {
  const text = row.status === '0' ? '启用' : '停用'
  ElMessageBox.confirm('确认要"' + text + '""' + row.roleName + '"角色吗?')
    .then(function () {
      return changeRoleStatus(row.roleId!, row.status!)
    })
    .then(() => {
      ElMessage.success(text + '成功')
    })
    .catch(function () {
      row.status = row.status === '0' ? '1' : '0'
    })
}

const config: any = ref<ArtCrudConfig<SysRole>>({
  api: {
    list: (params) => {
      let query = addDateRange(
        {
          ...queryParams.value,
          pageNum: params.pageNum,
          pageSize: params.pageSize
        },
        params.createTime
      )
      delete query.createTime
      return listRole(query)
    },
    add: (data) => {
      data.menuIds = getMenuAllCheckedKeys()
      return addRole(data)
    },
    update: (data) => {
      data.menuIds = getMenuAllCheckedKeys()
      return updateRole(data)
    }
  },
  searchConfig: {
    items: [
      {
        label: '角色名称',
        key: 'roleName',
        type: 'input',
        placeholder: '请输入角色名称'
      },
      {
        label: '权限字符',
        key: 'roleKey',
        type: 'input',
        placeholder: '请输入权限字符'
      },
      {
        label: '角色状态',
        key: 'status',
        type: 'input',
        placeholder: '请输入角色状态',
        options: sys_normal_disable
      },
      {
        label: '创建时间',
        key: 'createTime',
        type: 'daterange',
        props: {
          type: 'daterange',
          valueFormat: 'YYYY-MM-DD',
          rangeSeparator: '至',
          startPlaceholder: '开始日期',
          endPlaceholder: '结束日期'
        }
      }
    ]
  },
  columns: [
    { type: 'selection', label: '选择' },
    { prop: 'roleId', label: '角色ID' },
    { prop: 'roleName', label: '角色名称' },
    { prop: 'roleKey', label: '权限字符' },
    { prop: 'roleSort', label: '角色排序' },
    {
      prop: 'status',
      label: '角色状态',
      useSlot: true,
      slotName: 'statusSlot'
    },
    { prop: 'createTime', label: '创建时间', minWidth: 170 },
    {
      prop: 'operation',
      label: '操作',
      fixed: 'right',
      align: 'center',
      useSlot: true,
      slotName: 'operationTemplate',
      width: 240
    }
  ],
  editConfig: {
    span: 24,
    labelWidth: 100,
    items: [
      {
        label: '角色名称',
        key: 'roleName',
        type: 'input',
        placeholder: '请输入角色名称'
      },
      {
        label: '权限字符',
        tooltip:
          "控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasRole('admin')`)",
        labelWidth: '100px',
        key: 'roleKey',
        type: 'input',
        placeholder: '请输入权限字符',
        slots: {}
      },
      {
        label: '角色排序',
        key: 'roleSort',
        type: 'number',
        placeholder: '请输入角色排序'
      },
      {
        key: 'status',
        label: '状态',
        type: 'radiogroup',
        span: 12,
        options: sys_normal_disable
      },
      {
        label: '菜单权限',
        key: 'menuIds',
        type: 'treeselect',
        useSlot: true,
        slotName: 'menuIdsSlot'
      },
      {
        label: '备注',
        key: 'remark',
        type: 'textarea',
        placeholder: '请输入备注'
      }
    ],
    rules: {
      roleName: [
        { required: true, message: '角色名称不能为空', trigger: 'blur' }
      ],
      roleKey: [
        { required: true, message: '权限字符不能为空', trigger: 'blur' }
      ],
      roleSort: [
        { required: true, message: '角色顺序不能为空', trigger: 'blur' }
      ]
    }
  },
  btnConfig: {
    showAddOperation: () => hasAuth('system:role:add'),
    showDeleteOperation: (row) =>
      row.roleId !== 1 && hasAuth('system:role:remove'),
    showEditOperation: (row) => row.roleId !== 1 && hasAuth('system:role:edit')
  }
})
</script>

<style scoped lang="scss"></style>
