<template>
  <SystemCrudPage
    title="报警类别管理"
    permission="business:alertInfo"
    id-key="id"
    :fields="fields"
    :list-fn="fetchSqAlertInfoList"
    :get-fn="fetchSqAlertInfo"
    :add-fn="addSqAlertInfo"
    :update-fn="updateSqAlertInfo"
    :remove-fn="removeSqAlertInfo"
    :defaults="{ isDeleted: '0', status: '0', isopen: '0', isSend: '0', isReceive: '0' }"
    export-url="/business/sq/alert-info/export"
    drawer-size="820px"
  />
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addSqAlertInfo,
    fetchSqAlertInfo,
    fetchSqAlertInfoList,
    removeSqAlertInfo,
    updateSqAlertInfo
  } from '@/api/business/sqSafety'

  defineOptions({ name: 'BusinessSqAlertInfo' })

  const fields = computed(() => [
    {
      prop: 'types',
      label: '检测类型',
      type: 'select',
      dictType: 'alert_vol',
      required: true,
      search: true,
      minWidth: 150
    },
    {
      prop: 'level',
      label: '报警等级',
      type: 'select',
      dictType: 'level_info',
      required: true,
      search: true,
      minWidth: 120
    },
    {
      prop: 'text',
      label: '报警语音文字',
      required: true,
      span: 24,
      minWidth: 180,
      props: { type: 'textarea', rows: 3 }
    },
    {
      prop: 'isopen',
      label: '是否播报',
      type: 'select',
      dictType: 'flag',
      search: true,
      minWidth: 100
    },
    { prop: 'isReceive', label: '接收日志', type: 'select', dictType: 'flag', minWidth: 100 },
    { prop: 'isSend', label: '是否推送', type: 'select', dictType: 'flag', minWidth: 100 },
    {
      prop: 'sendUser',
      label: '推送人员',
      span: 24,
      minWidth: 150,
      props: { type: 'textarea', rows: 2 }
    },
    { prop: 'voice', label: '声音文件', span: 24, minWidth: 160 },
    { prop: 'name', label: '对应溜井', minWidth: 130 },
    { prop: 'status', label: '报警状态', minWidth: 100 },
    { prop: 'createTime', label: '创建时间', form: false, minWidth: 160 },
    { prop: 'updateTime', label: '更新时间', form: false, minWidth: 160 }
  ])
</script>
