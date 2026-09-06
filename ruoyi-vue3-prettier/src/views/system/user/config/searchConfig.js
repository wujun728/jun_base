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
      {
        label: '部门',
        field: 'deptId',
        type: 'treeSelect',
        options: ref([]),
        config: {
          props: { label: 'label', value: 'id', children: 'children' },
          checkStrictly: true,
          clearable: true,
        },
      },
      {
        label: '用户状态',
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
    elFormConfig: {},
  }
}
