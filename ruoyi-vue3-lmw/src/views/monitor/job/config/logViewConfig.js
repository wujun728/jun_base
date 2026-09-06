export default (listeners = {}) => {
  return {
    formItems: [
      {
        field: 'jobLogId',
        type: 'custom',
        label: '日志序号：',
      },
      {
        field: 'jobName',
        type: 'custom',
        label: '任务名称：',
      },

      {
        field: 'jobGroup',
        type: 'custom',
        label: '任务分组：',
      },
      {
        field: 'createTime',
        type: 'custom',
        label: '执行时间：',
      },

      {
        field: 'invokeTarget',
        type: 'custom',
        label: '调用方法：',
        layout: {
          xl: 24,
          lg: 24,
          md: 24,
          sm: 24,
          xs: 24,
        },
      },
      {
        field: 'jobMessage',
        type: 'custom',
        label: '日志信息：',
        layout: {
          xl: 24,
          lg: 24,
          md: 24,
          sm: 24,
          xs: 24,
        },
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
        label: '执行状态：',
        layout: {
          xl: 24,
          lg: 24,
          md: 24,
          sm: 24,
          xs: 24,
        },
      },
      {
        field: 'exceptionInfo',
        type: 'custom',
        label: '异常信息：',
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
