export default (config = {}) => {
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
      status: [{ required: true, message: '请选择状态', trigger: 'change' }],
    },
    formItems: [
      {
        field: 'roleName',
        type: 'input',
        label: '角色名称',
        config: {
          allowClear: false,
          maxlength: 30,
        },
      },
      {
        field: 'roleKey',
        type: 'input',
        label: '权限字符',
        config: {
          maxlength: 30,
        },
      },
      {
        label: '角色顺序',
        field: 'roleSort',
        type: 'inputNumber',
        config: {
          min: 0,
        },
      },
      {
        field: 'status',
        type: 'radio',
        options: [],
        label: '状态',
        isGroup: true,
        config: {
          optionType: 'button',
          buttonStyle: 'solid',
        },
      },
      {
        field: 'menuIds',
        type: 'tree',
        label: '菜单权限',
        options: ref([]),
        config: {
          checkable: true,
          fieldNames: { title: 'label', key: 'id', children: 'children' },
          checkStrictly: config.checkStrictly,
          defaultExpandAll: config.defaultExpandAll,
        },
      },
      {
        field: 'remark',
        type: 'textarea',
        label: '备注',
        config: {
          maxlength: 30,
        },
      },
    ],
    colLayout: {
      xxl: 24,
      xl: 24,
      lg: 24,
      md: 24,
      sm: 24,
      xs: 24,
    },
    formConfig: {
      layout: 'horizontal',
      labelCol: { style: { width: '80px' } },
      wrapperCol: { flex: 1 },
    },
  }
}
