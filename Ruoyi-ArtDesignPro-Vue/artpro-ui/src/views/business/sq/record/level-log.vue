<template>
  <div class="art-full-height">
    <SystemCrudPage
      title="料位一览表"
      permission="business:levelLog"
      id-key="id"
      :fields="fields"
      :list-fn="fetchSqLevelLogList"
      :get-fn="fetchSqLevelLog"
      :add-fn="addSqLevelLog"
      :update-fn="updateSqLevelLog"
      :remove-fn="removeSqLevelLog"
      export-url="/business/sq/level-log/export"
      drawer-size="720px"
    />
  </div>
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addSqLevelLog,
    fetchSqChuteOptions,
    fetchSqLevelLog,
    fetchSqLevelLogList,
    removeSqLevelLog,
    updateSqLevelLog
  } from '@/api/business/sqRecord'

  defineOptions({ name: 'BusinessSqLevelLog' })

  const chuteOptions = ref<Array<{ label: string; value: string }>>([])

  const fields = computed(() => [
    {
      prop: 'dates',
      label: '日期',
      type: 'date',
      search: false,
      minWidth: 120,
      props: dateProps()
    },
    { prop: 'times', label: '时间点', type: 'number', search: true, width: 100 },
    {
      prop: 'lname',
      label: '溜井点',
      type: 'select',
      options: chuteOptions.value,
      search: true,
      table: false
    },
    { prop: 'chuteName', label: '溜井点', form: false, minWidth: 150 },
    { prop: 'cars', label: '车数', type: 'number', minWidth: 90 },
    { prop: 'heights', label: '料位高度', type: 'number', minWidth: 110 },
    {
      prop: 'beginTime',
      label: '开始日期',
      type: 'date',
      form: false,
      search: true,
      table: false,
      props: dateProps()
    },
    {
      prop: 'endTime',
      label: '结束日期',
      type: 'date',
      form: false,
      search: true,
      table: false,
      props: dateProps()
    }
  ])

  onMounted(loadChutes)

  async function loadChutes() {
    const rows = await fetchSqChuteOptions()
    chuteOptions.value = rows.map((item) => ({
      label: item.lname || String(item.id),
      value: String(item.id)
    }))
  }

  function dateProps() {
    return { type: 'date', valueFormat: 'YYYY-MM-DD' }
  }
</script>
