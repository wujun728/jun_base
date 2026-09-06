export default () => {
  return {
    formItems: [
      {
        label: '字典名称',
        field: 'dictName',
        type: 'input',
        config: {
          allowClear: true,
        },
      },
      {
        label: '字典类型',
        field: 'dictType',
        type: 'input',
        config: {
          allowClear: true,
        },
      },
      {
        label: '状态',
        field: 'status',
        type: 'select',
        options: [],
        config: {
          allowClear: true,
        },
      },
      {
        label: '创建时间',
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
