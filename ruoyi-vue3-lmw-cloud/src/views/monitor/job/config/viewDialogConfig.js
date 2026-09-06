export default (listeners = {}) => {
  return {
    formItems: [
      {
        field: 'jobId',
        type: 'custom',
        label: '任务编号：',
      },
      {
        field: 'jobGroup',
        type: 'custom',
        label: '任务分组：',
      },

      {
        field: 'jobName',
        type: 'custom',
        label: '任务名称：',
      },
      {
        field: 'createTime',
        type: 'custom',
        label: '创建时间：',
      },

      {
        field: 'cronExpression',
        type: 'custom',
        label: 'cron表达式：',
      },
      {
        field: 'nextValidTime',
        type: 'custom',
        label: '下次执行时间：',
      },
      {
        field: 'invokeTarget',
        type: 'custom',
        label: '调用目标方法：',
        layout: {
          xl: 24,
          lg: 24,
          md: 24,
          sm: 24,
          xs: 24,
        },
      },
      {
        field: 'status',
        type: 'custom',
        label: '任务状态：',
      },
      {
        field: 'concurrent',
        type: 'custom',
        label: '是否并发：',
      },
      {
        field: 'misfirePolicy',
        type: 'custom',
        label: '执行策略：',
      },
    ],
    colLayout: {
      xl: 12,
      lg: 12,
      md: 12,
      sm: 12,
      xs: 24,
    },
    itemStyle: {
      padding: '0px 8px 0px 8px',
    },
    elFormConfig: {
      labelWidth: '120px',
    },
  }
}
