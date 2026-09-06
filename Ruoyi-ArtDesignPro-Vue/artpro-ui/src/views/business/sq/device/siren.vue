<template>
  <div class="art-full-height">
    <SystemCrudPage
      title="声光报警器管理"
      permission="business:deviceSiren"
      id-key="id"
      :fields="fields"
      :list-fn="fetchSqDeviceSirenList"
      :get-fn="fetchSqDeviceSiren"
      :add-fn="addSqDeviceSiren"
      :update-fn="updateSqDeviceSiren"
      :remove-fn="removeSqDeviceSiren"
      :defaults="{ flag: '0', isWarning: '0' }"
      export-url="/business/sq/device-siren/export"
      drawer-size="760px"
    />
  </div>
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addSqDeviceSiren,
    fetchSqDeviceSiren,
    fetchSqDeviceSirenList,
    removeSqDeviceSiren,
    updateSqDeviceSiren
  } from '@/api/business/sqDevice'

  defineOptions({ name: 'BusinessSqDeviceSiren' })

  const fields = computed(() => [
    { prop: 'name', label: '设备名称', required: true, search: true, minWidth: 130 },
    { prop: 'location', label: '位置', search: true, minWidth: 130 },
    { prop: 'ip', label: 'IP地址', required: true, search: true, minWidth: 130 },
    {
      prop: 'isWarning',
      label: '报警',
      type: 'select',
      dictType: 'flag',
      search: true,
      minWidth: 90
    },
    { prop: 'flag', label: '是否发送', type: 'select', dictType: 'flag', minWidth: 100 },
    { prop: 'volume', label: '音量', minWidth: 90 },
    { prop: 'status', label: '状态', minWidth: 100 },
    {
      prop: 'cameras',
      label: '关联摄像头',
      span: 24,
      minWidth: 180,
      props: { type: 'textarea', rows: 3 }
    },
    { prop: 'mark', label: '备注', span: 24, minWidth: 160, props: { type: 'textarea', rows: 3 } }
  ])
</script>
