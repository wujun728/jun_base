<template>
  <div class="art-full-height">
    <SystemCrudPage
      title="出料口记录"
      permission="business:outLog"
      id-key="id"
      :fields="fields"
      :list-fn="fetchSqOutLogList"
      :get-fn="fetchSqOutLog"
      :add-fn="addSqOutLog"
      :update-fn="updateSqOutLog"
      :remove-fn="removeSqOutLog"
      export-url="/business/sq/out-log/export"
      drawer-size="960px"
    />
  </div>
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addSqOutLog,
    fetchSqChuteOptions,
    fetchSqOutLog,
    fetchSqOutLogList,
    removeSqOutLog,
    updateSqOutLog
  } from '@/api/business/sqRecord'

  defineOptions({ name: 'BusinessSqOutLog' })

  const chuteOptions = ref<Array<{ label: string; value: string }>>([])
  const scheduleOptions = [
    { label: '白班', value: '白班' },
    { label: '夜班', value: '夜班' }
  ]

  const fields = computed(() => [
    { prop: 'ydNo', label: '运单编号', search: true, minWidth: 130 },
    { prop: 'leader', label: '班组长', minWidth: 100 },
    { prop: 'plateNumber', label: '车号', search: true, minWidth: 100 },
    { prop: 'taskTime', label: '任务日期', minWidth: 120 },
    {
      prop: 'schedule',
      label: '班次',
      type: 'select',
      options: scheduleOptions,
      search: true,
      width: 100
    },
    { prop: 'carType', label: '矿车类型', minWidth: 110 },
    {
      prop: 'wellingPoint',
      label: '溜井点',
      type: 'select',
      options: chuteOptions.value,
      search: true,
      table: false
    },
    { prop: 'wellingPointName', label: '溜井点', form: false, minWidth: 150 },
    { prop: 'mineralType', label: '矿种', search: true, minWidth: 110 },
    { prop: 'miners', label: '放矿工', minWidth: 100 },
    { prop: 'discharge', label: '放矿车数', minWidth: 110 },
    { prop: 'weighingTime', label: '称重时间', minWidth: 150 },
    { prop: 'totalTonnage', label: '合计吨位', minWidth: 110 },
    { prop: 'unloadingPoint', label: '卸矿点', minWidth: 120 },
    { prop: 'unloadingTypes', label: '卸矿矿种', minWidth: 120 },
    {
      prop: 'beginTime',
      label: '称重开始',
      type: 'datetime',
      form: false,
      search: true,
      table: false,
      props: dateTimeProps()
    },
    {
      prop: 'endTime',
      label: '称重结束',
      type: 'datetime',
      form: false,
      search: true,
      table: false,
      props: dateTimeProps()
    },
    { prop: 'createTime', label: '创建时间', form: false, minWidth: 160 }
  ])

  onMounted(loadChutes)

  async function loadChutes() {
    const rows = await fetchSqChuteOptions()
    chuteOptions.value = rows.map((item) => ({
      label: item.lname || String(item.id),
      value: String(item.id)
    }))
  }

  function dateTimeProps() {
    return { type: 'datetime', valueFormat: 'YYYY-MM-DD HH:mm:ss' }
  }
</script>
