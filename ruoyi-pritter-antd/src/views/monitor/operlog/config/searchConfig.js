export default () => {
  return {
    formItems: [
      {
        label: '操作地址',
        field: 'operIp',
        type: 'input',
        config: { allowClear: true },
      },
      {
        label: '系统模块',
        field: 'title',
        type: 'input',
        config: { allowClear: true },
      },
      {
        label: '操作人员',
        field: 'operName',
        type: 'input',
        config: { allowClear: true },
      },
      {
        label: '类型',
        field: 'businessType',
        type: 'select',
        options: [],
        config: { allowClear: true },
      },
      {
        label: '状态',
        field: 'status',
        type: 'select',
        options: [],
        config: { allowClear: true },
      },
      {
        label: '操作时间',
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
