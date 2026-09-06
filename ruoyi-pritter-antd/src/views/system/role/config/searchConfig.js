export default () => {
  return {
    formItems: [
      {
        label: '角色名称',
        field: 'roleName',
        type: 'input',
        config: {
          placeholder: '角色名称',
          allowClear: true,
        },
      },
      {
        label: '权限字符',
        field: 'roleKey',
        type: 'input',
        config: {
          placeholder: '权限字符',
          allowClear: true,
        },
      },
      {
        label: '角色状态',
        field: 'status',
        type: 'select',
        options: [],
        config: {
          allowClear: true,
        },
      },
      {
        label: '创建时间',
        field: 'dateRange',
        type: 'datepicker',
        config: {
          range: true,
          valueFormat: 'YYYY-MM-DD',
          format: 'YYYY/MM/DD',
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
