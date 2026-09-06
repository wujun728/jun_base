<template>
  <SystemCrudPage
    title="溜井卸矿日报表"
    permission="business:dispatchDayReport"
    id-key="id"
    :fields="fields"
    :list-fn="fetchSqDispatchReportList"
    :get-fn="fetchSqDispatchReport"
    :add-fn="noopAction"
    :update-fn="noopAction"
    :remove-fn="noopAction"
    drawer-size="860px"
  />
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    fetchSqDispatchReport,
    fetchSqDispatchReportList,
    noopAction
  } from '@/api/business/sqReport'

  defineOptions({ name: 'BusinessSqDispatchDayReport' })

  const fields = computed(() => [
    {
      prop: 'theday',
      label: '报表日期',
      type: 'date',
      search: true,
      minWidth: 120,
      props: dateProps()
    },
    { prop: 'lname', label: '溜井名称', search: true, minWidth: 150 },
    { prop: 'company', label: '所属公司', search: true, minWidth: 120 },
    { prop: 'bigc', label: '8点班大卡', minWidth: 110 },
    { prop: 'bigck', label: '8点班小卡', minWidth: 110 },
    { prop: 'bigf', label: '4点班大卡', minWidth: 110 },
    { prop: 'smailc', label: '4点班小卡', minWidth: 110 },
    { prop: 'smailck', label: '0点班大卡', minWidth: 110 },
    { prop: 'smailf', label: '0点班小卡', minWidth: 110 }
  ])

  function dateProps() {
    return { type: 'date', valueFormat: 'YYYY-MM-DD' }
  }
</script>
