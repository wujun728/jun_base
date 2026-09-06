<template>
  <div class="art-full-height">
    <div
      class="box-border flex h-full gap-4 max-md:block max-md:h-auto max-md:gap-0">
      <div
        class="h-full w-58 flex-shrink-0 max-md:mb-5 max-md:h-auto max-md:w-full">
        <ElCard class="tree-card art-card-xs mt-0 flex h-full flex-col">
          <template #header>
            <b>组织机构</b>
          </template>
          <ElScrollbar>
            <ElTree
              :data="deptOptions"
              node-key="id"
              default-expand-all
              highlight-current
              @node-click="handleNodeClick" />
          </ElScrollbar>
        </ElCard>
      </div>

      <div class="flex min-w-0 flex-grow flex-col">
        <ArtCrud
          :key="queryParams.deptId"
          ref="crudRef"
          v-model:search="queryParams"
          v-model:edit-form="form"
          :config="crudConfig"
          @add-row="handleAdd"
          @edit-row="handleUpdate"
          @selection-change="handleSelectionChange">
          <template #toolbar>
            <el-button
              v-auth="'system:user:edit'"
              type="success"
              plain
              :icon="Edit"
              :disabled="single"
              @click="() => handleUpdate()">
              修改
            </el-button>
            <el-button
              v-auth="'system:user:remove'"
              type="danger"
              plain
              :icon="Delete"
              :disabled="multiple"
              @click="() => handleDelete()">
              删除
            </el-button>
            <el-button
              v-auth="'system:user:import'"
              type="info"
              plain
              :icon="Upload"
              @click="() => handleImport()">
              导入
            </el-button>
            <el-button
              v-auth="'system:user:export'"
              type="warning"
              plain
              :icon="Download"
              @click="() => handleExport()">
              导出
            </el-button>
          </template>
          <template #col-userNameTemplate="{ row }">
            <a
              class="link-type"
              style="cursor: pointer"
              @click="handleViewData(row)">
              {{ row.userName }}
            </a>
          </template>
          <template #col-statusTemplate="{ row }">
            <el-switch
              v-model="row.status"
              active-value="0"
              inactive-value="1"
              @change="handleStatusChange(row)"></el-switch>
          </template>
          <template #col-operationTemplate="{ row }">
            <ArtButtonTable
              v-if="row.userId !== 1"
              v-auth="'system:user:resetPwd'"
              tooltip="重置密码"
              icon="ri:reset-left-line"
              iconClass="bg-warning/12 text-warning"
              @click="handleResetPwd(row)">
              重置密码
            </ArtButtonTable>
            <ArtButtonTable
              v-if="row.userId !== 1"
              v-auth="'system:user:edit'"
              tooltip="分配角色"
              icon="ri:fingerprint-fill"
              iconClass="bg-amber-100 text-amber-900"
              @click="handleAuthRole(row)">
              分配角色
            </ArtButtonTable>
          </template>
        </ArtCrud>
      </div>
    </div>

    <!-- 用户详情抽屉 -->
    <UserViewDrawer ref="userViewRef" />
    <!-- 分配角色抽屉 -->
    <AuthRoleDrawer ref="authRoleRef" @success="refreshTable" />
    <!-- 用户导入对话框 -->
    <excel-import-dialog
      ref="importUserRef"
      title="用户导入"
      action="/system/user/importData"
      template-action="/system/user/importTemplate"
      template-file-name="user_template"
      update-support-label="是否更新已经存在的用户数据"
      @success="refreshTable" />
  </div>
</template>

<script setup lang="ts">
import { Delete, Edit, Download, Upload } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'

import ArtCrud from '@/components/business/art-crud/index.vue'
import ExcelImportDialog from '@/components/core/ruoyi/excel-import-dialog/index.vue'
import { useAuth } from '@/hooks/core/useAuth'
import { useDict } from '@/hooks/core/useDict'
import { usePasswordRule } from '@/hooks/core/usePassword'
import {
  SysPost,
  SysRole,
  SysUser,
  TreeSelect,
  UserQueryParams
} from '@/types/api'
import download from '@/utils/http/download'
import { addDateRange } from '@/utils/sys/ruoyi'

import AuthRoleDrawer from './components/authRole.vue'
import UserViewDrawer from './components/view.vue'

const { hasAuth } = useAuth()
const { pwdValidator, pwdPromptValidator } = usePasswordRule()

import {
  addUser,
  changeUserStatus,
  delUser,
  deptTreeSelect,
  getConfigKey,
  getUser,
  listUser,
  resetUserPwd,
  updateUser
} from './api'

const userViewRef = ref<InstanceType<typeof UserViewDrawer>>()
const importUserRef = ref<InstanceType<typeof ExcelImportDialog>>()
const crudRef = ref<InstanceType<typeof ArtCrud>>()
const { sys_normal_disable, sys_user_sex } = useDict(
  'sys_normal_disable',
  'sys_user_sex'
)

const ids = ref<number[]>([])
const single = ref<boolean>(true)
const multiple = ref<boolean>(true)
const authRoleRef = ref<InstanceType<typeof AuthRoleDrawer>>()
const deptOptions = ref<TreeSelect[]>([])
const enabledDeptOptions = ref<TreeSelect[]>([])
const postOptions = ref<SysPost[]>([])
const roleOptions = ref<SysRole[]>([])
const form = ref<SysUser>({
  userId: undefined,
  deptId: undefined,
  userName: undefined,
  nickName: undefined,
  email: undefined,
  phonenumber: undefined,
  sex: undefined,
  status: undefined,
  password: undefined,
  avatar: undefined
})
const initPassword = ref('')

const queryParams = ref({
  userName: undefined,
  phonenumber: undefined,
  status: undefined,
  createTime: '',
  deptId: undefined
})
const rules = ref({
  userName: [
    { required: true, message: '用户名称不能为空', trigger: 'blur' },
    {
      min: 2,
      max: 20,
      message: '用户名称长度必须介于 2 和 20 之间',
      trigger: 'blur'
    }
  ],
  nickName: [{ required: true, message: '用户昵称不能为空', trigger: 'blur' }],
  email: [
    {
      type: 'email',
      message: '请输入正确的邮箱地址',
      trigger: ['blur', 'change']
    }
  ],
  phonenumber: [
    {
      pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
      message: '请输入正确的手机号码',
      trigger: 'blur'
    }
  ]
})

/** 选择条数  */
function handleSelectionChange(selection: SysUser[]) {
  ids.value = selection.map((item) => item.userId!)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
async function handleAdd() {
  const res = await getUser()
  postOptions.value = res.posts
  roleOptions.value = res.roles
  form.value.password = initPassword.value
}

/** 修改按钮操作 */
function handleUpdate(row?: SysUser) {
  const userId = row?.userId || ids.value[0]
  if (userId === 1) {
    ElMessage.error('系统管理员不能修改')
    return
  }
  getUser(userId).then((response) => {
    form.value = response.data!
    postOptions.value = response.posts
    roleOptions.value = response.roles
    form.value.postIds = response.postIds
    form.value.roleIds = response.roleIds
    form.value.password = ''
  })
  if (crudRef.value) {
    crudRef.value.openEdit(form.value)
  }
}

/** 用户状态修改  */
function handleStatusChange(row: SysUser) {
  const text = row.status === '0' ? '启用' : '停用'
  ElMessageBox.confirm(
    '确认要"' + text + '""' + row.userName + '"用户吗?',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
    .then(function () {
      changeUserStatus(row.userId!, row.status!)
    })
    .then(() => {
      ElMessage.success(text + '成功')
    })
    .catch(function () {
      row.status = row.status === '0' ? '1' : '0'
    })
}

/** 删除按钮操作 */
function handleDelete(row?: SysUser) {
  const userIds = row?.userId || ids.value
  ElMessageBox.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？')
    .then(function () {
      return delUser(userIds)
    })
    .then(() => {
      refreshTable()
      ElMessage.success('删除成功')
    })
    .catch(() => {})
}

/** 导入按钮操作 */
function handleImport() {
  if (importUserRef.value) {
    importUserRef.value.open()
  }
}

/** 导出按钮操作 */
function handleExport() {
  download(
    'system/user/export',
    {
      ...queryParams.value
    },
    `user_${new Date().getTime()}.xlsx`
  )
}

/** 重置密码按钮操作 */
function handleResetPwd(row: SysUser) {
  ElMessageBox.prompt(`请输入「${row.userName}」的新密码`, '重置密码', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    closeOnClickModal: false,
    inputValidator: pwdPromptValidator
  })
    .then(({ value }: { value: string }) => {
      resetUserPwd(row.userId!, value).then(() => {
        ElMessage.success('修改成功，新密码是：' + value)
      })
    })
    .catch(() => {})
}

/** 打开分配角色抽屉 */
function handleAuthRole(row: SysUser) {
  authRoleRef.value?.open(row.userId!)
}

/** 查询部门下拉树结构 */
async function getDeptTree() {
  const res = await deptTreeSelect()
  deptOptions.value = res
  enabledDeptOptions.value = filterDisabledDept(JSON.parse(JSON.stringify(res)))
}

/** 过滤禁用的部门 */
function filterDisabledDept(deptList: TreeSelect[]) {
  return deptList.filter((dept) => {
    if (dept.disabled) {
      return false
    }
    if (dept.children && dept.children.length) {
      dept.children = filterDisabledDept(dept.children)
    }
    return true
  })
}

/** 节点单击事件 */
function handleNodeClick(data: any) {
  queryParams.value.deptId = data.id
  // handleQuery()
}

/** 刷新表格 */
function refreshTable() {
  if (crudRef.value) {
    crudRef.value.handleRefresh()
  }
}

/** 详情按钮操作 */
function handleViewData(row: SysUser) {
  if (userViewRef.value) {
    userViewRef.value.open(row.userId as number)
  }
}

const crudConfig: any = computed(() => {
  return {
    api: {
      list: (params) => {
        console.log('111111')
        let query = addDateRange(
          {
            ...queryParams.value,
            pageNum: params.pageNum,
            pageSize: params.pageSize
          },
          params.createTime
        )
        delete query.createTime
        return listUser(query)
      },
      delete: (data) => delUser(data.userId),
      add: (data) => addUser(data),
      update: (data) => updateUser(data),
      batchDelete: (data) => delUser(data)
    },
    columns: [
      { type: 'selection', label: '选择' },
      { prop: 'userId', label: '用户ID' },
      {
        prop: 'userName',
        label: '用户名称',
        useSlot: true,
        slotName: 'userNameTemplate'
      },
      { prop: 'nickName', label: '用户昵称' },
      { prop: 'phonenumber', label: '手机号码', width: 140 },
      { prop: 'dept.deptName', label: '部门名称' },
      {
        prop: 'status',
        label: '状态',
        useSlot: true,
        slotName: 'statusTemplate'
      },
      { prop: 'createTime', label: '创建时间', width: 170 },
      {
        prop: 'operation',
        label: '操作',
        useSlot: true,
        slotName: 'operationTemplate',
        width: 210
      }
    ],
    searchConfig: {
      items: [
        {
          label: '用户名称',
          key: 'userName',
          type: 'input',
          placeholder: '请输入用户名称'
        },
        {
          label: '手机号码',
          key: 'phonenumber',
          type: 'input',
          placeholder: '请输入手机号码'
        },
        {
          label: '状态',
          key: 'status',
          type: 'select',
          placeholder: '请选择状态',
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
    editConfig: {
      labelWidth: 100,
      items: [
        {
          key: 'nickName',
          label: '用户昵称',
          type: 'input',
          span: 12,
          props: { placeholder: '请输入用户昵称', maxlength: 30 }
        },
        {
          key: 'deptId',
          label: '归属部门',
          type: 'treeselect',
          span: 12,
          props: {
            data: enabledDeptOptions.value,
            props: { value: 'id', label: 'label', children: 'children' },
            valueKey: 'id',
            placeholder: '请选择归属部门',
            clearable: true,
            checkStrictly: true
          }
        },
        {
          key: 'phonenumber',
          label: '手机号码',
          type: 'input',
          span: 12,
          props: { placeholder: '请输入手机号码', maxlength: 11 }
        },
        {
          key: 'email',
          label: '邮箱',
          type: 'input',
          span: 12,
          props: { placeholder: '请输入邮箱', maxlength: 50 }
        },
        {
          key: 'userName',
          label: '用户名称',
          type: 'input',
          span: 12,
          hidden: form.value.userId !== undefined,
          props: { placeholder: '请输入用户名称', maxlength: 30 }
        },
        {
          key: 'password',
          label: '用户密码',
          type: 'input',
          span: 12,
          hidden: form.value.userId !== undefined,
          props: {
            placeholder: '请输入用户密码',
            type: 'password',
            maxlength: 20,
            showPassword: true
          }
        },
        {
          key: 'sex',
          label: '用户性别',
          type: 'select',
          span: 12,
          props: {
            placeholder: '请选择',
            options: sys_user_sex.value
          }
        },
        {
          key: 'status',
          label: '状态',
          type: 'radiogroup',
          span: 12,
          options: sys_normal_disable.value
        },
        {
          key: 'postIds',
          label: '岗位',
          type: 'select',
          span: 12,
          props: {
            multiple: true,
            placeholder: '请选择',
            options: postOptions.value.map((item) => ({
              label: item.postName,
              value: item.postId,
              disabled: item.status === '1'
            }))
          }
        },
        {
          key: 'roleIds',
          label: '角色',
          type: 'select',
          span: 12,
          props: {
            multiple: true,
            placeholder: '请选择',
            options: roleOptions.value.map((item) => ({
              label: item.roleName,
              value: item.roleId,
              disabled: item.status === '1'
            }))
          }
        },
        {
          key: 'remark',
          label: '备注',
          type: 'input',
          span: 24,
          props: { type: 'textarea', placeholder: '请输入内容' }
        }
      ],
      rules: rules.value
    },
    btnConfig: {
      showAddOperation: () => hasAuth('sys:user:add'),
      showEditOperation: (row) => row.userId !== 1 && hasAuth('sys:user:edit'),
      showDeleteOperation: (row) =>
        row.userId !== 1 && hasAuth('sys:user:remove')
    }
  }
})

onMounted(async () => {
  getDeptTree()
  const res = await getConfigKey('sys.user.initPassword')
  initPassword.value = res
})
</script>
