export default () => {
  return {
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
        label: '创建时间',
        field: 'dateRange',
        type: 'datepicker',
        config: {
          range: true,
        },
      },
    ],
  }
}
