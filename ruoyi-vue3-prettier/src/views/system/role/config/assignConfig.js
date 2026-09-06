export default (listeners = {}) => {
  return {
    itemStyle: { padding: '0px 0px 0px 0px' },
    rules: {
      roleName: [
        { required: true, message: '角色名称不能为空', trigger: 'blur' },
      ],
      roleKey: [
        { required: true, message: '权限字符不能为空', trigger: 'blur' },
      ],
      roleSort: [
        { required: true, message: '角色顺序不能为空', trigger: 'blur' },
      ],
    },
    formItems: [
      {
        field: 'roleName',
        type: 'input',
        label: '角色名称',
        config: {
          disabled: true,
        },
      },
      {
        field: 'roleKey',
        type: 'input',
        label: '权限字符',
        config: {
          disabled: true,
        },
      },
      {
        label: '权限范围',
        field: 'dataScope',
        type: 'select',
        options: [
          { value: '1', label: '全部数据权限' },
          { value: '2', label: '自定数据权限' },
          { value: '3', label: '本部门数据权限' },
          { value: '4', label: '本部门及以下数据权限' },
          { value: '5', label: '仅本人数据权限' },
        ],
        eventFunction: {
          change: listeners.dataScopeChange,
        },
      },
      {
        field: 'deptIds',
        type: 'tree',
        label: '数据权限',
        options: ref([]),
        config: {
          showCheckbox: true,
          nodeKey: 'id',
          props: { label: 'label', children: 'children' },
          defaultExpandAll: true,
        },
      },
    ],
    colLayout: {
      xl: 24,
      lg: 24,
      md: 24,
      sm: 24,
      xs: 24,
    },
    elFormConfig: {
      labelWidth: '80px',
    },
  }
}
