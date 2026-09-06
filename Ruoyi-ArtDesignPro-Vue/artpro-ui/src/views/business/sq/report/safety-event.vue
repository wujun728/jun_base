<template>
  <SystemCrudPage
    title="安全事件统计"
    permission="business:safetyEventStat"
    id-key="statDate"
    :fields="fields"
    :list-fn="fetchSqSafetyEventStatList"
    :add-fn="noopAction"
    :update-fn="noopAction"
    :remove-fn="noopAction"
    drawer-size="680px"
  />
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import { fetchSqSafetyEventStatList, noopAction } from '@/api/business/sqReport'

  defineOptions({ name: 'BusinessSqSafetyEventStat' })

  const fields = computed(() => [
    { prop: 'statDate', label: '统计日期', minWidth: 120 },
    { prop: 'status', label: '报警类型', search: true, minWidth: 120 },
    { prop: 'state', label: '处理状态', search: true, minWidth: 120 },
    { prop: 'eventCount', label: '事件数量', minWidth: 110 },
    {
      prop: 'beginTime',
      label: '开始时间',
      type: 'datetime',
      form: false,
      search: true,
      table: false,
      props: dateTimeProps()
    },
    {
      prop: 'endTime',
      label: '结束时间',
      type: 'datetime',
      form: false,
      search: true,
      table: false,
      props: dateTimeProps()
    }
  ])

  function dateTimeProps() {
    return { type: 'datetime', valueFormat: 'YYYY-MM-DD HH:mm:ss' }
  }
</script>
