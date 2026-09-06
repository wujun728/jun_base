export default () => {
  return {
    itemStyle: { padding: '0px 0px 0px 0px' },
    rules: {
      parentId: [
        { required: true, message: '上级部门不能为空', trigger: 'change' },
      ],
      deptName: [
        { required: true, message: '部门名称不能为空', trigger: 'blur' },
      ],
      orderNum: [
        { required: true, message: '显示排序不能为空', trigger: 'blur' },
      ],
      email: [
        {
          type: 'email',
          message: '请输入正确的邮箱地址',
          trigger: ['blur', 'change'],
        },
      ],
      phone: [
        {
          pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
          message: '请输入正确的手机号码',
          trigger: 'blur',
        },
      ],
    },
    formItems: [
      {
        field: 'parentId',
        type: 'treeSelect',
        options: ref([]),
        label: '上级部门',
        config: {
          fieldNames: {
            label: 'deptName',
            value: 'deptId',
            children: 'children',
          },
          allowClear: false,
          treeDefaultExpandAll: true,
          showSearch: true,
          treeNodeFilterProp: 'deptName',
        },
        layout: {
          xl: 24,
          lg: 24,
          md: 24,
          sm: 24,
          xs: 24,
        },
      },
      {
        field: 'deptName',
        type: 'input',
        label: '部门名称',
        config: {
          allowClear: false,
          maxlength: 30,
        },
      },
      {
        field: 'orderNum',
        type: 'inputNumber',
        label: '显示排序',
        config: {
          min: 0,
        },
      },
      {
        field: 'leader',
        type: 'input',
        label: '负责人',
        config: {
          maxlength: 20,
        },
      },
      {
        field: 'phone',
        type: 'input',
        label: '联系电话',
        config: {
          maxlength: 11,
        },
      },
      {
        field: 'email',
        type: 'input',
        label: '邮箱',
        config: {
          maxlength: 50,
        },
      },
      {
        field: 'status',
        type: 'radio',
        label: '部门状态',
        isGroup: true,
        options: [],
        config: {
          optionType: 'button',
          buttonStyle: 'solid',
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
