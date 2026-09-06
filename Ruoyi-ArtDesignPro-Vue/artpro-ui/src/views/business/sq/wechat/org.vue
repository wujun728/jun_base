<template>
  <div class="art-full-height sq-wechat-org">
    <ElTabs v-model="activeTab">
      <ElTabPane label="企业微信用户" name="user">
        <SystemCrudPage
          title="企业微信用户"
          permission="business:wxOrg"
          id-key="userId"
          :fields="userFields"
          :list-fn="fetchSqWxUserList"
          :get-fn="fetchSqWxUser"
          :add-fn="addSqWxUser"
          :update-fn="updateSqWxUser"
          :remove-fn="removeSqWxUser"
          export-url="/business/sq/wx-user/export"
          drawer-size="720px"
        />
      </ElTabPane>
      <ElTabPane label="企业微信部门" name="dept">
        <SystemCrudPage
          title="企业微信部门"
          permission="business:wxOrg"
          id-key="deptId"
          :fields="deptFields"
          :list-fn="fetchSqWxDeptList"
          :get-fn="fetchSqWxDept"
          :add-fn="addSqWxDept"
          :update-fn="updateSqWxDept"
          :remove-fn="removeSqWxDept"
          :defaults="{ enabled: 'true', deptSort: '999' }"
          export-url="/business/sq/wx-dept/export"
          drawer-size="720px"
        />
      </ElTabPane>
    </ElTabs>
  </div>
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addSqWxDept,
    addSqWxUser,
    fetchSqWxDept,
    fetchSqWxDeptList,
    fetchSqWxDeptOptions,
    fetchSqWxUser,
    fetchSqWxUserList,
    removeSqWxDept,
    removeSqWxUser,
    updateSqWxDept,
    updateSqWxUser
  } from '@/api/business/sqWechat'

  defineOptions({ name: 'BusinessSqWxOrg' })

  const activeTab = ref('user')
  const deptOptions = ref<Array<{ label: string; value: string }>>([])
  const enabledOptions = [
    { label: '启用', value: 'true' },
    { label: '禁用', value: 'false' }
  ]

  const userFields = computed(() => [
    {
      prop: 'deptId',
      label: '部门',
      type: 'select',
      options: deptOptions.value,
      search: true,
      table: false
    },
    { prop: 'deptLabel', label: '部门', form: false, minWidth: 160 },
    { prop: 'username', label: '账号', required: true, search: true, minWidth: 130 },
    { prop: 'nickName', label: '姓名', required: true, search: true, minWidth: 130 },
    { prop: 'createTime', label: '创建时间', form: false, minWidth: 160 },
    { prop: 'updateTime', label: '更新时间', form: false, minWidth: 160 }
  ])

  const deptFields = computed(() => [
    {
      prop: 'pid',
      label: '上级部门',
      type: 'select',
      options: deptOptions.value,
      search: false,
      table: false
    },
    { prop: 'parentName', label: '上级部门', form: false, minWidth: 150 },
    { prop: 'name', label: '部门名称', required: true, search: true, minWidth: 160 },
    { prop: 'deptSort', label: '排序', required: true, minWidth: 90 },
    { prop: 'subCount', label: '子部门数', form: false, minWidth: 100 },
    {
      prop: 'enabled',
      label: '状态',
      type: 'select',
      options: enabledOptions,
      search: true,
      minWidth: 100
    },
    { prop: 'type', label: '组织类型', minWidth: 110 },
    { prop: 'createTime', label: '创建时间', form: false, minWidth: 160 },
    { prop: 'updateTime', label: '更新时间', form: false, minWidth: 160 }
  ])

  onMounted(loadDeptOptions)

  async function loadDeptOptions() {
    const rows = await fetchSqWxDeptOptions()
    deptOptions.value = rows.map((item) => ({
      label: item.name || String(item.deptId),
      value: String(item.deptId)
    }))
  }
</script>

<style scoped>
  .sq-wechat-org :deep(.el-tabs) {
    height: 100%;
  }

  .sq-wechat-org :deep(.el-tabs__content) {
    height: calc(100% - 56px);
  }

  .sq-wechat-org :deep(.el-tab-pane) {
    height: 100%;
  }
</style>
