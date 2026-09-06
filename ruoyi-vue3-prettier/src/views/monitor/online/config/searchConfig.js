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
    ],
  }
}
