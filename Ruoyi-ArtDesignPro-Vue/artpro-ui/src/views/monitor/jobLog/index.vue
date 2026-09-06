<template>
  <MonitorListPage
    permission="monitor:job"
    id-key="jobLogId"
    :fields="fields"
    :list-fn="listLogs"
    :remove-fn="removeJobLog"
    :clean-fn="cleanJobLogs"
    export-url="/monitor/jobLog/export"
  />
</template>
<script setup lang="ts">
  import MonitorListPage from '@/components/business/monitor-list-page/index.vue'
  import { cleanJobLogs, fetchJobLogList, removeJobLog } from '@/api/monitor'
  import { useDict } from '@/hooks/core/useDict'
  import type { Query } from '@/api/system/types'
  defineOptions({ name: 'MonitorJobLog' })
  const props = defineProps<{ jobId?: number }>()
  const { dict } = useDict('sys_job_group', 'sys_common_status')
  const listLogs = (params: Query) => fetchJobLogList({ ...params, jobId: props.jobId })
  const fields = computed(() => [
    { prop: 'jobLogId', label: '日志编号', width: 90 },
    { prop: 'jobName', label: '任务名称', search: true },
    { prop: 'jobGroup', label: '任务组名', search: true, dict: dict.sys_job_group },
    { prop: 'invokeTarget', label: '调用目标', minWidth: 180 },
    { prop: 'jobMessage', label: '日志信息', minWidth: 180 },
    { prop: 'status', label: '状态', dict: dict.sys_common_status },
    { prop: 'startTime', label: '开始时间', minWidth: 170 },
    { prop: 'endTime', label: '结束时间', minWidth: 170 }
  ])
</script>
