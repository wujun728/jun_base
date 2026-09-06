export default (otherConfig = {}) => {
  return {
    formItems: [
      {
        label: '登录地址',
        field: 'ipaddr',
        type: 'input',
      },
      {
        label: '用户名称',
        field: 'userName',
        type: 'input',
      },
      {
        label: '状态',
        field: 'status',
        type: 'select',
        options: ref([]),
      },
      {
        label: '操作时间',
        field: 'dateRange',
        type: 'datepicker',
        config: {
          range: true,
        },
      },
    ],
  }
}
