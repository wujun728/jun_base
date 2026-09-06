<template>
  <div class="box-border flex gap-4 h-full max-md:block">
    <ElCard class="w-58 flex-shrink-0 art-card-xs">
      <template #header><b>部门组织</b></template>
      <ElTree
        :data="deptTree"
        node-key="deptId"
        :props="{ label: 'deptName', children: 'children' }"
        default-expand-all
        highlight-current
        @node-click="selectDept"
      />
    </ElCard>
    <div class="min-w-0 flex-1">
      <SystemCrudPage
        ref="page"
        title="用户"
        permission="system:user"
        id-key="userId"
        :fields="fields"
        :list-fn="listUsers"
        :get-fn="getUser"
        :add-fn="addUser"
        :update-fn="updateUser"
        :remove-fn="removeUser"
        :status-fn="changeUserStatus"
        export-url="/system/user/export"
        :actions="actions"
        :defaults="{ status: '0', sex: '0' }"
      >
        <template #toolbar>
          <ElUpload
            v-if="hasAuth('system:user:import')"
            :auto-upload="false"
            :show-file-list="false"
            accept=".xlsx,.xls"
            @change="upload"
            ><ElButton>导入</ElButton></ElUpload
          >
          <ElButton v-if="hasAuth('system:user:import')" @click="template">下载模板</ElButton>
        </template>
      </SystemCrudPage>
    </div>
    <ElDialog v-model="roleVisible" title="分配角色" width="560px">
      <ElCheckboxGroup v-model="selectedRoles"
        ><ElCheckbox v-for="role in roles" :key="role.roleId" :value="role.roleId">{{
          role.roleName
        }}</ElCheckbox></ElCheckboxGroup
      >
      <template #footer
        ><ElButton @click="roleVisible = false">取消</ElButton
        ><ElButton type="primary" @click="saveRoles">保存</ElButton></template
      >
    </ElDialog>
  </div>
</template>
<script setup lang="ts">
  import { ElMessageBox, ElMessage, type UploadFile } from 'element-plus'
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import { download } from '@/api/common'
  import {
    addUser,
    changeUserStatus,
    fetchUser,
    fetchUserList,
    fetchUserRoles,
    importUsers,
    removeUser,
    resetUserPassword,
    updateUser,
    updateUserRoles
  } from '@/api/system/user'
  import { fetchDeptList } from '@/api/system/dept'
  import { buildTree } from '@/utils/system/tree'
  import { useAuth } from '@/hooks/core/useAuth'
  import { useDict } from '@/hooks/core/useDict'
  import type { Entity, Query } from '@/api/system/types'
  defineOptions({ name: 'SystemUser' })
  const { hasAuth } = useAuth(),
    { dict } = useDict('sys_normal_disable', 'sys_user_sex')
  const page = ref<any>(),
    selectedDept = ref<number>(),
    deptTree = ref<Entity[]>([])
  const roles = ref<Entity[]>([]),
    posts = ref<Entity[]>([]),
    selectedRoles = ref<number[]>([])
  const roleVisible = ref(false),
    currentUser = ref<Entity>()
  const fields = computed(() => [
    { prop: 'userName', label: '用户名称', required: true, search: true },
    { prop: 'nickName', label: '用户昵称', required: true },
    { prop: 'dept.deptName', label: '部门', form: false },
    {
      prop: 'deptId',
      label: '归属部门',
      type: 'treeselect',
      table: false,
      required: true,
      props: {
        data: deptTree.value,
        nodeKey: 'deptId',
        valueKey: 'deptId',
        props: { value: 'deptId', label: 'deptName', children: 'children' },
        checkStrictly: true
      }
    },
    { prop: 'phonenumber', label: '手机号码', search: true },
    { prop: 'email', label: '邮箱' },
    {
      prop: 'sex',
      label: '性别',
      type: 'select',
      options: dict.sys_user_sex,
      dict: dict.sys_user_sex
    },
    {
      prop: 'status',
      label: '状态',
      type: 'select',
      options: dict.sys_normal_disable,
      dict: dict.sys_normal_disable,
      search: true
    },
    { prop: 'password', label: '初始密码', table: false },
    {
      prop: 'postIds',
      label: '岗位',
      type: 'select',
      table: false,
      props: { multiple: true },
      options: posts.value.map((post) => ({ label: post.postName, value: post.postId }))
    },
    {
      prop: 'roleIds',
      label: '角色',
      type: 'select',
      table: false,
      props: { multiple: true },
      options: roles.value.map((role) => ({ label: role.roleName, value: role.roleId }))
    },
    { prop: 'createTime', label: '创建时间', form: false }
  ])
  const actions = [
    {
      key: 'password',
      label: '重置密码',
      auth: 'system:user:resetPwd',
      icon: 'ri:lock-password-line',
      handler: resetPassword
    },
    {
      key: 'roles',
      label: '分配角色',
      auth: 'system:user:edit',
      icon: 'ri:user-settings-line',
      handler: openRoles
    }
  ]
  const listUsers = (params: Query) => fetchUserList({ ...params, deptId: selectedDept.value })
  async function getUser(id: number) {
    const result = await fetchUser(id)
    roles.value = result.roles || roles.value
    posts.value = result.posts || posts.value
    return { ...result.data, roleIds: result.roleIds, postIds: result.postIds }
  }
  async function init() {
    deptTree.value = buildTree(await fetchDeptList(), 'deptId', 'parentId')
    const result = await fetchUser()
    roles.value = result.roles || []
    posts.value = result.posts || []
  }
  function selectDept(row: Entity) {
    selectedDept.value = row.deptId
    page.value?.refreshData()
  }
  async function resetPassword(row: Entity) {
    const { value } = await ElMessageBox.prompt(`请输入“${row.userName}”的新密码`, '重置密码', {
      inputPattern: /^.{5,20}$/,
      inputErrorMessage: '密码长度为 5 到 20 位'
    })
    await resetUserPassword({ userId: row.userId, password: value })
  }
  async function openRoles(row: Entity) {
    currentUser.value = row
    const result = await fetchUserRoles(row.userId)
    roles.value = result.roles || []
    selectedRoles.value = (result.user?.roles || []).map((role: Entity) => role.roleId)
    roleVisible.value = true
  }
  async function saveRoles() {
    await updateUserRoles(currentUser.value!.userId, selectedRoles.value)
    roleVisible.value = false
    page.value?.refreshData()
  }
  async function upload(file: UploadFile) {
    if (!file.raw) return
    await importUsers(file.raw, true)
    ElMessage.success('导入成功')
    page.value?.refreshData()
  }
  function template() {
    download('/system/user/importTemplate', {}, '用户导入模板.xlsx')
  }
  init()
</script>
