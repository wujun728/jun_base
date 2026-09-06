<template>
  <MonitorListPage
    permission="monitor:logininfor"
    id-key="infoId"
    :fields="fields"
    :list-fn="fetchLogininforList"
    :remove-fn="removeLogininfor"
    :clean-fn="cleanLogininfors"
    export-url="/monitor/logininfor/export"
    :actions="actions"
  />
</template>
<script setup lang="ts">
  import MonitorListPage from '@/components/business/monitor-list-page/index.vue'
  import {
    cleanLogininfors,
    fetchLogininforList,
    removeLogininfor,
    unlockUser
  } from '@/api/monitor'
  import { useDict } from '@/hooks/core/useDict'
  import type { Entity } from '@/api/system/types'
  defineOptions({ name: 'MonitorLogininfor' })
  const { dict } = useDict('sys_common_status')
  const fields = computed(() => [
    { prop: 'infoId', label: '访问编号', width: 90 },
    { prop: 'userName', label: '用户名称', search: true },
    { prop: 'ipaddr', label: '登录地址', search: true },
    { prop: 'loginLocation', label: '登录地点' },
    { prop: 'browser', label: '浏览器' },
    { prop: 'os', label: '操作系统' },
    { prop: 'status', label: '状态', dict: dict.sys_common_status },
    { prop: 'msg', label: '提示消息' },
    { prop: 'loginTime', label: '登录时间', minWidth: 170 }
  ])
  const actions = [
    {
      key: 'unlock',
      label: '账户解锁',
      auth: 'monitor:logininfor:unlock',
      icon: 'ri:lock-unlock-line',
      handler: (row: Entity) => unlockUser(row.userName)
    }
  ]
</script>
