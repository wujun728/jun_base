export default (otherConfig = {}) => {
  return {
    formItems: [
      {
        label: '用户名称',
        field: 'userName',
        type: 'input',
      },
      {
        label: '手机号码',
        field: 'phonenumber',
        type: 'input',
      },
    ],
    colLayout: {
      xl: 7,
      lg: 7,
      md: 7,
      sm: 12,
      xs: 24,
    },
    footerLayout: {
      xl: 6,
      lg: 7,
      md: 7,
      sm: 12,
      xs: 24,
    },
  }
}
