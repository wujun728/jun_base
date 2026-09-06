<template>
  <SystemCrudPage
    title="溜井作业监测"
    permission="business:safetyAlert"
    id-key="id"
    :fields="fields"
    :list-fn="fetchSqSafetyAlertList"
    :get-fn="fetchSqSafetyAlert"
    :add-fn="addSqSafetyAlert"
    :update-fn="updateSqSafetyAlert"
    :remove-fn="removeSqSafetyAlert"
    :defaults="{ isDeleted: '0', state: '0' }"
    export-url="/business/sq/safety-alert/export"
    drawer-size="900px"
  />
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import { fetchSqChuteOptions } from '@/api/business/sqRecord'
  import {
    addSqSafetyAlert,
    fetchSqSafetyAlert,
    fetchSqSafetyAlertList,
    removeSqSafetyAlert,
    updateSqSafetyAlert
  } from '@/api/business/sqSafety'

  defineOptions({ name: 'BusinessSqSafetyAlert' })

  const chuteOptions = ref<Array<{ label: string; value: string }>>([])

  const fields = computed(() => [
    {
      prop: 'status',
      label: '检测类型',
      type: 'select',
      dictType: 'alert_info',
      search: true,
      minWidth: 150
    },
    {
      prop: 'name',
      label: '溜井',
      type: 'select',
      options: chuteOptions.value,
      search: true,
      table: false
    },
    { prop: 'chuteName', label: '溜井名称', form: false, minWidth: 150 },
    { prop: 'tnameLabel', label: '所属单位', form: false, minWidth: 120 },
    { prop: 'company', label: '所属中段', form: false, minWidth: 120 },
    {
      prop: 'state',
      label: '报警状态',
      type: 'select',
      dictType: 'xwjc_status',
      search: true,
      minWidth: 110
    },
    { prop: 'ip', label: '摄像头IP', search: true, minWidth: 130 },
    { prop: 'img', label: '图片', span: 24, minWidth: 180 },
    {
      prop: 'handle',
      label: '处理记录',
      span: 24,
      minWidth: 160,
      props: { type: 'textarea', rows: 3 }
    },
    {
      prop: 'handleTime',
      label: '处理时间',
      type: 'datetime',
      minWidth: 160,
      props: dateTimeProps()
    },
    {
      prop: 'beginTime',
      label: '报警开始',
      type: 'datetime',
      form: false,
      search: true,
      table: false,
      props: dateTimeProps()
    },
    {
      prop: 'endTime',
      label: '报警结束',
      type: 'datetime',
      form: false,
      search: true,
      table: false,
      props: dateTimeProps()
    },
    { prop: 'createTime', label: '报警时间', form: false, minWidth: 160 }
  ])

  onMounted(async () => {
    const rows = await fetchSqChuteOptions()
    chuteOptions.value = rows.map((item) => ({
      label: item.lname || String(item.id),
      value: String(item.id)
    }))
  })

  function dateTimeProps() {
    return { type: 'datetime', valueFormat: 'YYYY-MM-DD HH:mm:ss' }
  }
</script>
