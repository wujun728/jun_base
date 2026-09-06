<template>
  <SystemCrudPage
    title="溜井卸矿 班次日报表"
    permission="business:dispatchShiftReport"
    id-key="id"
    :fields="fields"
    :list-fn="fetchSqDispatchReportList"
    :get-fn="fetchSqDispatchReport"
    :add-fn="noopAction"
    :update-fn="noopAction"
    :remove-fn="noopAction"
    drawer-size="900px"
  />
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    fetchSqDispatchReport,
    fetchSqDispatchReportList,
    noopAction
  } from '@/api/business/sqReport'

  defineOptions({ name: 'BusinessSqDispatchShiftReport' })

  const fields = computed(() => reportFields(true))

  function reportFields(showShift: boolean) {
    const dateProps = { type: 'date', valueFormat: 'YYYY-MM-DD' }
    return [
      {
        prop: 'theday',
        label: '报表日期',
        type: 'date',
        search: true,
        minWidth: 120,
        props: dateProps
      },
      { prop: 'lname', label: '溜井名称', search: true, minWidth: 150 },
      { prop: 'company', label: '所属公司', search: true, minWidth: 120 },
      { prop: 'bigc', label: showShift ? '8点班大卡' : '大卡', minWidth: 110 },
      { prop: 'bigck', label: showShift ? '8点班小卡' : '小卡', minWidth: 110 },
      { prop: 'bigf', label: '4点班大卡', hidden: !showShift, minWidth: 110 },
      { prop: 'smailc', label: '4点班小卡', hidden: !showShift, minWidth: 110 },
      { prop: 'smailck', label: '0点班大卡', hidden: !showShift, minWidth: 110 },
      { prop: 'smailf', label: '0点班小卡', hidden: !showShift, minWidth: 110 },
      { prop: 'bigcKuang', label: '大卡矿石', minWidth: 110 },
      { prop: 'bigcFei', label: '大卡废石', minWidth: 110 },
      { prop: 'bigckKuang', label: '小卡矿石', minWidth: 110 },
      { prop: 'bigckFei', label: '小卡废石', minWidth: 110 }
    ]
  }
</script>
