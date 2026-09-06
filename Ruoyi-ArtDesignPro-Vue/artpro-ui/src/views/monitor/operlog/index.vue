<template>
  <MonitorListPage
    permission="monitor:operlog"
    id-key="operId"
    :fields="fields"
    :list-fn="fetchOperLogList"
    :remove-fn="removeOperLog"
    :clean-fn="cleanOperLogs"
    :transform-params="transformParams"
    export-url="/monitor/operlog/export"
  />
</template>
<script setup lang="ts">
  import MonitorListPage from '@/components/business/monitor-list-page/index.vue'
  import { cleanOperLogs, fetchOperLogList, removeOperLog } from '@/api/monitor'
  import { useDict } from '@/hooks/core/useDict'
  import type { Entity, Query } from '@/api/system/types'
  defineOptions({ name: 'MonitorOperlog' })
  const { dict } = useDict('sys_oper_type', 'sys_common_status')
  const dictOptions = (items: Array<{ dictLabel: string; dictValue: string }> = []) =>
    items.map((item: { dictLabel: string; dictValue: string }) => ({
      label: item.dictLabel,
      value: item.dictValue
    }))
  const fields = computed(() => [
    { prop: 'operId', label: '日志编号', width: 90 },
    { prop: 'title', label: '系统模块', search: true },
    {
      prop: 'businessType',
      label: '操作类型',
      dict: dict.sys_oper_type,
      search: {
        type: 'select',
        props: { options: dictOptions(dict.sys_oper_type), clearable: true }
      }
    },
    { prop: 'operName', label: '操作人员', search: true },
    { prop: 'operIp', label: '操作地址', search: true },
    { prop: 'operLocation', label: '操作地点' },
    {
      prop: 'status',
      label: '状态',
      dict: dict.sys_common_status,
      search: {
        type: 'select',
        props: { options: dictOptions(dict.sys_common_status), clearable: true }
      }
    },
    { prop: 'operTime', label: '操作时间', minWidth: 170 },
    {
      prop: 'operTimeRange',
      label: '操作时间',
      table: false,
      detail: false,
      search: {
        type: 'datetimerange',
        props: {
          type: 'datetimerange',
          valueFormat: 'YYYY-MM-DD HH:mm:ss',
          rangeSeparator: '至',
          startPlaceholder: '开始时间',
          endPlaceholder: '结束时间'
        }
      }
    },
    { prop: 'operUrl', label: '请求 URL', table: false },
    { prop: 'method', label: '后端方法', table: false },
    { prop: 'requestMethod', label: '请求方式', table: false },
    { prop: 'operParam', label: '请求参数', table: false },
    { prop: 'jsonResult', label: '返回参数', table: false },
    { prop: 'errorMsg', label: '错误消息', table: false },
    {
      prop: 'costTime',
      label: '消耗时间',
      table: false,
      formatter: (row: Entity) => `${row.costTime ?? 0} 毫秒`
    }
  ])

  function transformParams(params: Entity): Query {
    const { operTimeRange, ...result } = params
    if (Array.isArray(operTimeRange)) {
      result['params[beginTime]'] = operTimeRange[0]
      result['params[endTime]'] = operTimeRange[1]
    }
    return result
  }
</script>
