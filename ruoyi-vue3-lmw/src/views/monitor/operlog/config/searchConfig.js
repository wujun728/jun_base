export default (otherConfig = {}) => {
  return {
    formItems: [
      {
        label: '操作地址',
        field: 'operIp',
        type: 'input',
      },
      {
        label: '系统模块',
        field: 'title',
        type: 'input',
      },
      {
        label: '操作人员',
        field: 'operName',
        type: 'input',
      },
      {
        label: '类型',
        field: 'businessType',
        type: 'select',
        options: [],
      },
      {
        label: '状态',
        field: 'status',
        type: 'select',
        options: [],
      },
      {
        label: '操作时间',
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
