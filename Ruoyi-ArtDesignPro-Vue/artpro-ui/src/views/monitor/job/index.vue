<template>
  <div class="art-full-height">
    <SystemCrudPage
      title="定时任务"
      permission="monitor:job"
      id-key="jobId"
      :fields="fields"
      :list-fn="fetchJobList"
      :get-fn="fetchJob"
      :add-fn="addJob"
      :update-fn="updateJob"
      :remove-fn="removeJob"
      :status-fn="changeJobStatus"
      export-url="/monitor/job/export"
      :actions="actions"
      :defaults="{ status: '0', jobGroup: 'DEFAULT', concurrent: '1', misfirePolicy: '3' }"
    />
    <ElDrawer v-model="visible" :title="`${current.jobName || ''} - 调度日志`" size="86%">
      <JobLog v-if="visible" :job-id="current.jobId" />
    </ElDrawer>
  </div>
</template>
<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import JobLog from '@/views/monitor/jobLog/index.vue'
  import {
    addJob,
    changeJobStatus,
    fetchJob,
    fetchJobList,
    removeJob,
    runJob,
    updateJob
  } from '@/api/monitor'
  import { useDict } from '@/hooks/core/useDict'
  import type { Entity } from '@/api/system/types'
  defineOptions({ name: 'MonitorJob' })
  const { dict } = useDict('sys_job_group', 'sys_job_status')
  const visible = ref(false),
    current = reactive<Entity>({})
  const fields = computed(() => [
    { prop: 'jobName', label: '任务名称', required: true, search: true },
    {
      prop: 'jobGroup',
      label: '任务组名',
      type: 'select',
      options: dict.sys_job_group,
      dict: dict.sys_job_group,
      required: true,
      search: true
    },
    { prop: 'invokeTarget', label: '调用目标', required: true },
    { prop: 'cronExpression', label: 'Cron 表达式', required: true },
    {
      prop: 'misfirePolicy',
      label: '执行策略',
      type: 'select',
      table: false,
      options: [
        { label: '默认', value: '0' },
        { label: '立即执行', value: '1' },
        { label: '执行一次', value: '2' },
        { label: '放弃执行', value: '3' }
      ]
    },
    {
      prop: 'concurrent',
      label: '并发执行',
      type: 'select',
      table: false,
      options: [
        { label: '允许', value: '0' },
        { label: '禁止', value: '1' }
      ]
    },
    {
      prop: 'status',
      label: '状态',
      type: 'select',
      options: dict.sys_job_status,
      dict: dict.sys_job_status
    },
    { prop: 'remark', label: '备注', table: false }
  ])
  const actions = [
    {
      key: 'run',
      label: '执行一次',
      auth: 'monitor:job:changeStatus',
      icon: 'ri:play-line',
      handler: (row: Entity) => runJob({ jobId: row.jobId, jobGroup: row.jobGroup })
    },
    {
      key: 'logs',
      label: '调度日志',
      auth: 'monitor:job:query',
      icon: 'ri:file-list-3-line',
      handler: (row: Entity) => {
        Object.assign(current, row)
        visible.value = true
      }
    }
  ]
</script>
