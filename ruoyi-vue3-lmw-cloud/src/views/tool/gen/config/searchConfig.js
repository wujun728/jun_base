export default (otherConfig = {}) => {
  return {
    itemStyle: { padding: '15px 20px 0px 0px' },
    formItems: [
      {
        label: '表名称',
        field: 'tableName',
        type: 'input',
      },
      {
        label: '表描述',
        field: 'tableComment',
        type: 'input',
      },
      {
        label: '表名称',
        field: 'tableName',
        type: 'input',
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
