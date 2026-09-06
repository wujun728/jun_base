<template>
  <div class="art-full-height">
    <SystemCrudPage
      ref="page"
      title="角色"
      permission="system:role"
      id-key="roleId"
      :fields="fields"
      :list-fn="fetchRoleList"
      :get-fn="getRole"
      :add-fn="saveNew"
      :update-fn="saveEdit"
      :remove-fn="removeRole"
      :status-fn="changeRoleStatus"
      export-url="/system/role/export"
      :actions="actions"
      :defaults="{ status: '0', roleSort: 0, menuCheckStrictly: true }"
    />
    <ElDrawer v-model="scopeVisible" title="数据权限" size="520px">
      <ElForm label-width="110px"
        ><ElFormItem label="角色名称">{{ scope.roleName }}</ElFormItem>
        <ElFormItem label="权限范围"
          ><ElSelect v-model="scope.dataScope" class="w-full"
            ><ElOption v-for="item in scopes" :key="item.value" v-bind="item" /></ElSelect
        ></ElFormItem>
        <ElFormItem v-if="scope.dataScope === '2'" label="部门权限"
          ><ElTree
            ref="deptTreeRef"
            :data="deptTree"
            node-key="id"
            show-checkbox
            default-expand-all
            :default-checked-keys="deptChecked"
        /></ElFormItem>
      </ElForm>
      <template #footer
        ><ElButton @click="scopeVisible = false">取消</ElButton
        ><ElButton type="primary" @click="saveScope">保存</ElButton></template
      >
    </ElDrawer>
    <ElDrawer v-model="usersVisible" title="授权用户" size="760px">
      <ElButton type="primary" class="mb-3" @click="openSelectUsers">添加用户</ElButton>
      <ElTable :data="allocated"
        ><ElTableColumn prop="userName" label="用户名称" /><ElTableColumn
          prop="nickName"
          label="用户昵称"
        /><ElTableColumn prop="phonenumber" label="手机号码" /><ElTableColumn label="操作"
          ><template #default="{ row }"
            ><ElButton link type="danger" @click="cancel(row)">取消授权</ElButton></template
          ></ElTableColumn
        ></ElTable
      >
    </ElDrawer>
    <ElDialog v-model="selectVisible" title="选择用户" width="760px">
      <ElTable ref="selectTable" :data="unallocated" @selection-change="selectedUsers = $event"
        ><ElTableColumn type="selection" /><ElTableColumn
          prop="userName"
          label="用户名称" /><ElTableColumn prop="nickName" label="用户昵称" /><ElTableColumn
          prop="phonenumber"
          label="手机号码"
      /></ElTable>
      <template #footer
        ><ElButton @click="selectVisible = false">取消</ElButton
        ><ElButton type="primary" @click="saveUsers">授权</ElButton></template
      >
    </ElDialog>
  </div>
</template>
<script setup lang="ts">
  import { h } from 'vue'
  import { ElTree } from 'element-plus'
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addRole,
    cancelRoleUser,
    changeRoleStatus,
    fetchAllocatedUsers,
    fetchRole,
    fetchRoleDeptTree,
    fetchRoleList,
    fetchUnallocatedUsers,
    removeRole,
    selectRoleUsers,
    updateRole,
    updateRoleDataScope
  } from '@/api/system/role'
  import { fetchMenuTree, fetchRoleMenuTree } from '@/api/system/menu'
  import { useDict } from '@/hooks/core/useDict'
  import type { Entity } from '@/api/system/types'
  defineOptions({ name: 'SystemRole' })
  const { dict } = useDict('sys_normal_disable')
  const page = ref<any>(),
    menuTreeRef = ref<any>(),
    deptTreeRef = ref<any>()
  const menuTree = ref<Entity[]>([]),
    menuChecked = ref<number[]>([])
  const scopeVisible = ref(false),
    usersVisible = ref(false),
    selectVisible = ref(false)
  const scope = reactive<Entity>({}),
    deptTree = ref<Entity[]>([]),
    deptChecked = ref<number[]>([])
  const allocated = ref<Entity[]>([]),
    unallocated = ref<Entity[]>([]),
    selectedUsers = ref<Entity[]>([])
  const currentRole = ref<Entity>()
  const scopes = [
    { label: '全部数据权限', value: '1' },
    { label: '自定义数据权限', value: '2' },
    { label: '本部门数据权限', value: '3' },
    { label: '本部门及以下数据权限', value: '4' },
    { label: '仅本人数据权限', value: '5' }
  ]
  const fields = computed(() => [
    { prop: 'roleName', label: '角色名称', required: true, search: true },
    { prop: 'roleKey', label: '权限字符', required: true, search: true },
    { prop: 'roleSort', label: '显示顺序', type: 'number', required: true },
    {
      prop: 'status',
      label: '状态',
      type: 'select',
      options: dict.sys_normal_disable,
      dict: dict.sys_normal_disable,
      search: true
    },
    { prop: 'menuCheckStrictly', label: '菜单关联选择', type: 'switch', table: false },
    {
      prop: 'menuIds',
      label: '菜单权限',
      table: false,
      render: () =>
        h(ElTree, {
          ref: menuTreeRef,
          data: menuTree.value,
          nodeKey: 'id',
          showCheckbox: true,
          defaultExpandAll: true,
          defaultCheckedKeys: menuChecked.value
        })
    },
    { prop: 'remark', label: '备注', table: false },
    { prop: 'createTime', label: '创建时间', form: false }
  ])
  const actions = [
    {
      key: 'scope',
      label: '数据权限',
      auth: 'system:role:edit',
      icon: 'ri:shield-keyhole-line',
      handler: openScope
    },
    {
      key: 'users',
      label: '分配用户',
      auth: 'system:role:edit',
      icon: 'ri:user-add-line',
      handler: openUsers
    }
  ]
  async function init() {
    menuTree.value = await fetchMenuTree()
  }
  async function getRole(id: number) {
    const [role, tree] = await Promise.all([fetchRole(id), fetchRoleMenuTree(id)])
    menuTree.value = tree.menus || []
    menuChecked.value = tree.checkedKeys || []
    return role
  }
  function withMenus(data: Entity) {
    return {
      ...data,
      menuIds: [
        ...(menuTreeRef.value?.getCheckedKeys() || []),
        ...(menuTreeRef.value?.getHalfCheckedKeys() || [])
      ]
    }
  }
  const saveNew = (data: Entity) => addRole(withMenus(data)),
    saveEdit = (data: Entity) => updateRole(withMenus(data))
  async function openScope(row: Entity) {
    Object.assign(scope, row)
    const result = await fetchRoleDeptTree(row.roleId)
    deptTree.value = result.depts || []
    deptChecked.value = result.checkedKeys || []
    scopeVisible.value = true
  }
  async function saveScope() {
    await updateRoleDataScope({ ...scope, deptIds: deptTreeRef.value?.getCheckedKeys() || [] })
    scopeVisible.value = false
    page.value?.refreshData()
  }
  async function openUsers(row: Entity) {
    currentRole.value = row
    allocated.value = (
      await fetchAllocatedUsers({ roleId: row.roleId, pageNum: 1, pageSize: 100 })
    ).rows
    usersVisible.value = true
  }
  async function cancel(row: Entity) {
    await cancelRoleUser({ roleId: currentRole.value!.roleId, userId: row.userId })
    openUsers(currentRole.value!)
  }
  async function openSelectUsers() {
    unallocated.value = (
      await fetchUnallocatedUsers({ roleId: currentRole.value!.roleId, pageNum: 1, pageSize: 100 })
    ).rows
    selectVisible.value = true
  }
  async function saveUsers() {
    await selectRoleUsers(
      currentRole.value!.roleId,
      selectedUsers.value.map((user) => user.userId)
    )
    selectVisible.value = false
    openUsers(currentRole.value!)
  }
  init()
</script>
