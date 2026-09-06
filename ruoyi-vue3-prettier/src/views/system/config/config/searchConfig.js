export default (otherConfig = {}) => {
  return {
    formItems: [
      {
        label: '参数名称',
        field: 'configName',
        type: 'input',
      },
      {
        label: '参数键名',
        field: 'configKey',
        type: 'input',
      },
      {
        label: '系统内置',
        field: 'configType',
        type: 'select',
        options: [],
      },
      {
        label: '创建时间',
        field: 'dateRange',
        type: 'datepicker',
        config: {
          type: 'daterange',
          valueFormat: 'YYYY-MM-DD',
          format: 'YYYY/MM/DD',
        },
      },
    ],
  }
}
