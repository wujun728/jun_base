<template>
  <MonitorListPage
    permission="monitor:online"
    id-key="tokenId"
    :fields="fields"
    :list-fn="fetchOnlineList"
    :remove-fn="forceLogout"
  >
    <template #toolbar="{ refresh }">
      <ElButton
        v-if="hasAuth('monitor:online:forceLogout')"
        type="danger"
        @click="clearOnline(refresh)"
      >
        清空
      </ElButton>
    </template>
  </MonitorListPage>
</template>
<script setup lang="ts">
  import { ElMessageBox } from 'element-plus'
  import MonitorListPage from '@/components/business/monitor-list-page/index.vue'
  import { cleanOnlineUsers, fetchOnlineList, forceLogout } from '@/api/monitor'
  import { useAuth } from '@/hooks/core/useAuth'
  defineOptions({ name: 'MonitorOnline' })
  const { hasAuth } = useAuth()
  const fields = [
    { prop: 'tokenId', label: '会话编号', minWidth: 230 },
    { prop: 'userName', label: '用户名称', search: true },
    { prop: 'deptName', label: '部门' },
    { prop: 'ipaddr', label: '登录地址', search: true },
    { prop: 'loginLocation', label: '登录地点' },
    { prop: 'browser', label: '浏览器' },
    { prop: 'os', label: '操作系统' },
    { prop: 'loginTime', label: '登录时间', minWidth: 170, formatter: formatDateTime }
  ]

  function formatDateTime(row: Record<string, unknown>) {
    const date = new Date(Number(row.loginTime))
    if (Number.isNaN(date.getTime())) return '-'
    const pad = (value: number) => String(value).padStart(2, '0')
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`
  }

  async function clearOnline(refresh: () => void) {
    await ElMessageBox.confirm('确定清空其他在线用户会话吗？当前会话将保留。', '提示', {
      type: 'warning'
    })
    await cleanOnlineUsers()
    refresh()
  }
</script>
