export default (otherConfig = {}) => {
  return {
    itemStyle: { padding: '15px 20px 0px 0px' },
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
    ],
  }
}
