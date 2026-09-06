export default () => {
  return {
    formItems: [
      {
        label: '登录地址',
        field: 'ipaddr',
        type: 'input',
        config: { allowClear: true },
      },
      {
        label: '用户名称',
        field: 'userName',
        type: 'input',
        config: { allowClear: true },
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
