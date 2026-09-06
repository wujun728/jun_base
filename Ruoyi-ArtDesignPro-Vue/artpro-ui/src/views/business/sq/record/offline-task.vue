<template>
  <div class="art-full-height">
    <SystemCrudPage
      title="溜井离线任务"
      permission="business:offlineTask"
      id-key="id"
      :fields="fields"
      :list-fn="fetchSqOfflineTaskList"
      :get-fn="fetchSqOfflineTask"
      :add-fn="addSqOfflineTask"
      :update-fn="updateSqOfflineTask"
      :remove-fn="removeSqOfflineTask"
      :status-fn="updateSqOfflineTaskStatus"
      :defaults="{ status: '1' }"
      export-url="/business/sq/offline-task/export"
      drawer-size="760px"
    />
  </div>
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addSqOfflineTask,
    fetchSqChuteOptions,
    fetchSqOfflineTask,
    fetchSqOfflineTaskList,
    removeSqOfflineTask,
    updateSqOfflineTask,
    updateSqOfflineTaskStatus
  } from '@/api/business/sqRecord'

  defineOptions({ name: 'BusinessSqOfflineTask' })

  const chuteOptions = ref<Array<{ label: string; value: string }>>([])

  const fields = computed(() => [
    { prop: 'name', label: '任务名称', required: true, search: true, minWidth: 150 },
    {
      prop: 'startTime',
      label: '开始时间',
      type: 'datetime',
      required: true,
      minWidth: 160,
      props: dateTimeProps()
    },
    {
      prop: 'endTime',
      label: '结束时间',
      type: 'datetime',
      required: true,
      minWidth: 160,
      props: dateTimeProps()
    },
    {
      prop: 'lname',
      label: '溜井',
      type: 'select',
      options: chuteOptions.value,
      required: true,
      search: true,
      table: false
    },
    { prop: 'chuteName', label: '溜井名称', form: false, minWidth: 150 },
    { prop: 'channel', label: '任务通道号', required: true, search: true, minWidth: 120 },
    { prop: 'status', label: '状态', form: false, width: 100 },
    {
      prop: 'beginTime',
      label: '开始时间起',
      type: 'datetime',
      form: false,
      search: true,
      table: false,
      props: dateTimeProps()
    },
    {
      prop: 'searchEndTime',
      label: '开始时间止',
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
