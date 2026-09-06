<template>
  <div class="art-full-height">
    <SystemCrudPage
      title="设备运行记录"
      permission="business:deviceRecord"
      id-key="id"
      :fields="fields"
      :list-fn="fetchSqDeviceRecordList"
      :get-fn="fetchSqDeviceRecord"
      :add-fn="addSqDeviceRecord"
      :update-fn="updateSqDeviceRecord"
      :remove-fn="removeSqDeviceRecord"
      :defaults="{ isOpen: '1' }"
      export-url="/business/sq/device-record/export"
      drawer-size="760px"
    />
  </div>
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addSqDeviceRecord,
    fetchSqDeviceCameraOptions,
    fetchSqDeviceRecord,
    fetchSqDeviceRecordList,
    removeSqDeviceRecord,
    updateSqDeviceRecord
  } from '@/api/business/sqDevice'

  defineOptions({ name: 'BusinessSqDeviceRecord' })

  const chuteNameOptions = ref<Array<{ label: string; value: string }>>([])
  const openOptions = [
    { label: '上线', value: '1' },
    { label: '离线', value: '0' }
  ]

  const fields = computed(() => [
    { prop: 'channelId', label: '通道ID', search: true, minWidth: 120 },
    {
      prop: 'lname',
      label: '设备名称',
      type: 'select',
      options: chuteNameOptions.value,
      search: true,
      minWidth: 150
    },
    {
      prop: 'isOpen',
      label: '运行状态',
      type: 'select',
      options: openOptions,
      search: true,
      width: 110
    },
    { prop: 'carmip', label: '设备IP', search: true, minWidth: 130 },
    {
      prop: 'type',
      label: '设备类型',
      type: 'select',
      dictType: 'camera_type',
      search: true,
      minWidth: 120
    },
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
    },
    { prop: 'createTime', label: '创建时间', form: false, minWidth: 160 }
  ])

  onMounted(loadChutes)

  async function loadChutes() {
    const rows = await fetchSqDeviceCameraOptions()
    chuteNameOptions.value = rows.map((item) => ({
      label: item.lname || String(item.id),
      value: item.lname || String(item.id)
    }))
  }

  function dateTimeProps() {
    return { type: 'datetime', valueFormat: 'YYYY-MM-DD HH:mm:ss' }
  }
</script>
