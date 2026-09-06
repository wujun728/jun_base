export default () => {
  return {
    formItems: [
      {
        label: '参数名称',
        field: 'configName',
        type: 'input',
        config: {
          allowClear: true,
        },
      },
      {
        label: '参数键名',
        field: 'configKey',
        type: 'input',
        config: {
          allowClear: true,
        },
      },
      {
        label: '系统内置',
        field: 'configType',
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
