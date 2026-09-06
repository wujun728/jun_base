export default () => {
  return {
    formItems: [
      {
        label: '用户名称',
        field: 'userName',
        type: 'input',
        config: {
          allowClear: true,
        },
      },
      {
        label: '手机号码',
        field: 'phonenumber',
        type: 'input',
        config: {
          allowClear: true,
        },
      },
      {
        label: '部门',
        field: 'deptId',
        type: 'treeSelect',
        options: [],
        config: {
          fieldNames: { label: 'label', value: 'id', children: 'children' },
          allowClear: true,
          treeDefaultExpandAll: true,
        },
      },
      {
        label: '用户状态',
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
