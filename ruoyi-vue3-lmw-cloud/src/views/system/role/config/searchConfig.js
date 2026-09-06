export default (otherConfig = {}) => {
  return {
    formItems: [
      {
        label: '角色名称',
        field: 'roleName',
        type: 'input',
        config: {
          placeholder: '角色名称',
        },
      },
      {
        label: '权限字符',
        field: 'roleKey',
        type: 'input',
        config: {
          placeholder: '权限字符',
        },
      },
      {
        label: '角色状态',
        field: 'status',
        type: 'select',
        options: [],
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
