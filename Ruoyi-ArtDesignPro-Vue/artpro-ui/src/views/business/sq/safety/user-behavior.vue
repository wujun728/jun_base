<template>
  <SystemCrudPage
    title="人员行为监测"
    permission="business:userBehavior"
    id-key="id"
    :fields="fields"
    :list-fn="fetchSqUserBehaviorList"
    :get-fn="fetchSqUserBehavior"
    :add-fn="addSqUserBehavior"
    :update-fn="updateSqUserBehavior"
    :remove-fn="removeSqUserBehavior"
    :defaults="{ status: '0' }"
    export-url="/business/sq/user-behavior/export"
    drawer-size="900px"
  />
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import { fetchSqChuteOptions } from '@/api/business/sqRecord'
  import {
    addSqUserBehavior,
    fetchSqUserBehavior,
    fetchSqUserBehaviorList,
    removeSqUserBehavior,
    updateSqUserBehavior
  } from '@/api/business/sqSafety'

  defineOptions({ name: 'BusinessSqUserBehavior' })

  const chuteOptions = ref<Array<{ label: string; value: string }>>([])

  const fields = computed(() => behaviorFields('xwjc_type', '溜井名称', chuteOptions.value))

  onMounted(async () => {
    const rows = await fetchSqChuteOptions()
    chuteOptions.value = rows.map((item) => ({
      label: item.lname || String(item.id),
      value: item.lname || String(item.id)
    }))
  })

  function behaviorFields(
    dictType: string,
    nameLabel: string,
    options: Array<{ label: string; value: string }>
  ) {
    return [
      { prop: 'type', label: '检测类型', type: 'select', dictType, search: true, minWidth: 150 },
      { prop: 'name', label: nameLabel, type: 'select', options, search: true, minWidth: 140 },
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
        prop: 'remark',
        label: '备注',
        span: 24,
        minWidth: 160,
        props: { type: 'textarea', rows: 2 }
      },
      { prop: 'instruction', label: '指令', minWidth: 120 },
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
      { prop: 'ip', label: '设备IP', search: true, minWidth: 130 },
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
    ]
  }

  function dateTimeProps() {
    return { type: 'datetime', valueFormat: 'YYYY-MM-DD HH:mm:ss' }
  }
</script>
