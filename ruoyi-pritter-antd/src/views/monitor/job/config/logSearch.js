export default () => {
  return {
    formItems: [
      {
        label: '任务名称',
        field: 'jobName',
        type: 'input',
        config: { allowClear: true },
      },
      {
        label: '任务组名',
        field: 'jobGroup',
        type: 'select',
        options: [],
        config: { allowClear: true },
      },
      {
        label: '执行状态',
        field: 'status',
        type: 'select',
        options: [],
        config: { allowClear: true },
      },
      {
        label: '执行时间',
        field: 'dateRange',
        type: 'datepicker',
        config: {
          range: true,
          valueFormat: 'YYYY-MM-DD',
          format: 'YYYY/MM/DD',
        },
      },
    ],
    colLayout: {
      xxl: 4,
      xl: 6,
      lg: 8,
      md: 12,
      sm: 12,
      xs: 24,
    },
  }
}
