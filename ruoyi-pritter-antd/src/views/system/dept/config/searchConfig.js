export default () => {
  return {
    formItems: [
      {
        label: '部门名称',
        field: 'deptName',
        type: 'input',
        config: {
          allowClear: true,
        },
      },
      {
        label: '状态',
        field: 'status',
        type: 'select',
        options: [],
        config: {
          allowClear: true,
        },
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
