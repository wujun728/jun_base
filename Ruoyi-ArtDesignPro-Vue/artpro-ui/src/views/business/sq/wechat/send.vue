<template>
  <SystemCrudPage
    title="企业微信推送"
    permission="business:wxSend"
    id-key="id"
    :fields="fields"
    :list-fn="fetchSqWxSendList"
    :get-fn="fetchSqWxSend"
    :add-fn="addSqWxSend"
    :update-fn="updateSqWxSend"
    :remove-fn="removeSqWxSend"
    :defaults="{ isSend: '0' }"
    export-url="/business/sq/wx-send/export"
    drawer-size="820px"
  />
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addSqWxSend,
    fetchSqWxDeptOptions,
    fetchSqWxSend,
    fetchSqWxSendList,
    removeSqWxSend,
    updateSqWxSend
  } from '@/api/business/sqWechat'

  defineOptions({ name: 'BusinessSqWxSend' })

  const deptOptions = ref<Array<{ label: string; value: string }>>([])

  const fields = computed(() => [
    {
      prop: 'types',
      label: '报警类型',
      type: 'select',
      dictType: 'alert_vol',
      search: true,
      minWidth: 150
    },
    {
      prop: 'level',
      label: '报警等级',
      type: 'select',
      dictType: 'level_info',
      search: true,
      minWidth: 120
    },
    {
      prop: 'isSend',
      label: '是否推送',
      type: 'select',
      dictType: 'flag',
      search: true,
      minWidth: 110
    },
    {
      prop: 'deptId',
      label: '推送部门',
      type: 'select',
      options: deptOptions.value,
      search: true,
      table: false
    },
    { prop: 'deptLabel', label: '推送部门', form: false, minWidth: 150 },
    { prop: 'userName', label: '推送名称', search: true, minWidth: 140 },
    {
      prop: 'sendUser',
      label: '推送人员',
      span: 24,
      minWidth: 180,
      props: { type: 'textarea', rows: 3 }
    },
    { prop: 'tname', label: '所属单位', minWidth: 120 },
    { prop: 'createTime', label: '创建时间', form: false, minWidth: 160 },
    { prop: 'updateTime', label: '更新时间', form: false, minWidth: 160 }
  ])

  onMounted(loadDeptOptions)

  async function loadDeptOptions() {
    const rows = await fetchSqWxDeptOptions()
    deptOptions.value = rows.map((item) => ({
      label: item.name || String(item.deptId),
      value: String(item.deptId)
    }))
  }
</script>
