<template>
  <SystemCrudPage
    title="斜井作业监测"
    permission="business:inclinedShaft"
    id-key="id"
    :fields="fields"
    :list-fn="fetchSqInclinedShaftList"
    :get-fn="fetchSqInclinedShaft"
    :add-fn="addSqInclinedShaft"
    :update-fn="updateSqInclinedShaft"
    :remove-fn="removeSqInclinedShaft"
    :defaults="{ status: '0' }"
    export-url="/business/sq/inclined-shaft/export"
    drawer-size="860px"
  />
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addSqInclinedShaft,
    fetchSqInclinedShaft,
    fetchSqInclinedShaftList,
    removeSqInclinedShaft,
    updateSqInclinedShaft
  } from '@/api/business/sqSafety'

  defineOptions({ name: 'BusinessSqInclinedShaft' })

  const fields = computed(() => [
    {
      prop: 'type',
      label: '检测类型',
      type: 'select',
      dictType: 'xj_type',
      search: true,
      minWidth: 140
    },
    { prop: 'name', label: '斜井名称', search: true, minWidth: 140 },
    { prop: 'section', label: '所属中段', search: true, minWidth: 120 },
    {
      prop: 'warningTime',
      label: '警示时间',
      type: 'datetime',
      minWidth: 160,
      props: dateTimeProps()
    },
    { prop: 'picture', label: '图片', span: 24, minWidth: 160 },
    { prop: 'video', label: '视频', span: 24, minWidth: 160 },
    {
      prop: 'status',
      label: '状态',
      type: 'select',
      dictType: 'xwjc_status',
      search: true,
      minWidth: 100
    },
    {
      prop: 'handle',
      label: '处理记录',
      span: 24,
      minWidth: 150,
      props: { type: 'textarea', rows: 3 }
    },
    {
      prop: 'handleTime',
      label: '处理时间',
      type: 'datetime',
      minWidth: 160,
      props: dateTimeProps()
    },
    { prop: 'ip', label: '摄像头IP', search: true, minWidth: 130 },
    { prop: 'scene', label: '场景号', minWidth: 110 },
    {
      prop: 'beginTime',
      label: '警示开始',
      type: 'datetime',
      form: false,
      search: true,
      table: false,
      props: dateTimeProps()
    },
    {
      prop: 'endTime',
      label: '警示结束',
      type: 'datetime',
      form: false,
      search: true,
      table: false,
      props: dateTimeProps()
    },
    { prop: 'createTime', label: '创建时间', form: false, minWidth: 160 }
  ])

  function dateTimeProps() {
    return { type: 'datetime', valueFormat: 'YYYY-MM-DD HH:mm:ss' }
  }
</script>
