<template>
  <div class="art-full-height">
    <SystemCrudPage
      title="RFID阅读器管理"
      permission="business:rfidReader"
      id-key="id"
      :fields="fields"
      :list-fn="fetchSqRfidReaderList"
      :get-fn="fetchSqRfidReader"
      :add-fn="addSqRfidReader"
      :update-fn="updateSqRfidReader"
      :remove-fn="removeSqRfidReader"
      :defaults="{ status: '运行' }"
      export-url="/business/sq/rfid-reader/export"
      drawer-size="760px"
    />
  </div>
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addSqRfidReader,
    fetchSqDeviceCameraOptions,
    fetchSqRfidReader,
    fetchSqRfidReaderList,
    removeSqRfidReader,
    updateSqRfidReader
  } from '@/api/business/sqDevice'

  defineOptions({ name: 'BusinessSqRfidReader' })

  const chuteOptions = ref<Array<{ label: string; value: string }>>([])

  const fields = computed(() => [
    { prop: 'name', label: '名称', required: true, search: true, minWidth: 130 },
    { prop: 'location', label: '位置', search: true, minWidth: 140 },
    { prop: 'ip', label: 'IP地址', required: true, search: true, minWidth: 130 },
    { prop: 'username', label: '用户名', minWidth: 110 },
    { prop: 'psw', label: '密码', table: false, props: { showPassword: true } },
    { prop: 'port', label: '端口', minWidth: 90 },
    {
      prop: 'tname',
      label: '关联溜井',
      type: 'select',
      options: chuteOptions.value,
      search: true,
      table: false
    },
    { prop: 'tnameLabel', label: '关联溜井', form: false, minWidth: 150 },
    {
      prop: 'status',
      label: '状态',
      type: 'select',
      dictType: 'rfid_status',
      search: true,
      minWidth: 100
    },
    { prop: 'mark', label: '备注', span: 24, minWidth: 160, props: { type: 'textarea', rows: 3 } },
    { prop: 'createTime', label: '创建时间', form: false, minWidth: 160 }
  ])

  onMounted(loadChutes)

  async function loadChutes() {
    const rows = await fetchSqDeviceCameraOptions()
    chuteOptions.value = rows.map((item) => ({
      label: item.lname || String(item.id),
      value: String(item.id)
    }))
  }
</script>
