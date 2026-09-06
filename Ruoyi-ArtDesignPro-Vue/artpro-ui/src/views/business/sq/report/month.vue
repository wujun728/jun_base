<template>
  <SystemCrudPage
    title="溜井卸矿月度报表"
    permission="business:dispatchMonthReport"
    id-key="id"
    :fields="fields"
    :list-fn="fetchSqDispatchMonthReportList"
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
    fetchSqDispatchMonthReportList,
    fetchSqDispatchReport,
    noopAction
  } from '@/api/business/sqReport'

  defineOptions({ name: 'BusinessSqDispatchMonthReport' })

  const fields = computed(() => [
    {
      prop: 'theday',
      label: '报表月份',
      type: 'date',
      search: true,
      minWidth: 120,
      props: monthProps()
    },
    { prop: 'lname', label: '溜井名称', search: true, minWidth: 150 },
    { prop: 'company', label: '所属公司', search: true, minWidth: 120 },
    { prop: 'bigc', label: '大卡合计', minWidth: 110 },
    { prop: 'bigck', label: '小卡合计', minWidth: 110 },
    { prop: 'bigcKuang', label: '大卡矿石', minWidth: 110 },
    { prop: 'bigcFei', label: '大卡废石', minWidth: 110 },
    { prop: 'bigckKuang', label: '小卡矿石', minWidth: 110 },
    { prop: 'bigckFei', label: '小卡废石', minWidth: 110 }
  ])

  function monthProps() {
    return { type: 'month', valueFormat: 'YYYY-MM' }
  }
</script>
