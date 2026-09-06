export default (otherConfig = {}) => {
  return {
    formItems: [
      {
        label: '字典名称',
        field: 'dictName',
        type: 'input',
      },
      {
        label: '字典类型',
        field: 'dictType',
        type: 'input',
      },
      {
        label: '状态',
        field: 'status',
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
