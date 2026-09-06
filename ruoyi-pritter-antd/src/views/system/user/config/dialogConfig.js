export default () => {
  return {
    rules: {
      userName: [
        { required: true, message: '用户名称不能为空', trigger: 'blur' },
        {
          min: 2,
          max: 20,
          message: '用户名称长度必须介于 2 和 20 之间',
          trigger: 'blur',
        },
      ],
      nickName: [
        { required: true, message: '用户昵称不能为空', trigger: 'blur' },
      ],
      password: [
        { required: true, message: '密码不能为空', trigger: 'blur' },
        {
          min: 5,
          max: 20,
          message: '长度必须介于 5 和 20 之间',
          trigger: 'blur',
        },
        {
          pattern: /^[^<>"'|\\]+$/,
          message: '不能包含非法字符：< > " \' \\ |',
          trigger: 'blur',
        },
      ],
      email: [
        {
          type: 'email',
          message: '请输入正确的邮箱地址',
          trigger: ['blur', 'change'],
        },
      ],
      phonenumber: [
        {
          pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
          message: '请输入正确的手机号码',
          trigger: 'blur',
        },
      ],
    },
    formItems: [
      {
        field: 'nickName',
        type: 'input',
        label: '用户昵称',
        config: {
          allowClear: false,
          maxlength: 30,
        },
      },
      {
        label: '归属部门',
        field: 'deptId',
        type: 'treeSelect',
        options: ref([]),
        config: {
          fieldNames: { label: 'label', value: 'id', children: 'children' },
          allowClear: false,
          treeDefaultExpandAll: true,
        },
      },
      {
        field: 'phonenumber',
        type: 'input',
        label: '手机号码',
        config: {
          allowClear: false,
          maxlength: 11,
        },
      },
      {
        field: 'email',
        type: 'input',
        label: '邮箱',
        config: {
          allowClear: false,
          maxlength: 50,
        },
      },
      {
        field: 'userName',
        type: 'input',
        label: '用户名称',
        config: {
          allowClear: false,
          maxlength: 30,
        },
      },
      {
        field: 'password',
        type: 'input',
        label: '用户密码',
        config: {
          type: 'password',
          allowClear: false,
          maxlength: 20,
        },
      },
      {
        field: 'sex',
        type: 'select',
        label: '性别',
        options: [],
        config: {
          allowClear: false,
        },
      },
      {
        field: 'status',
        type: 'radio',
        label: '状态',
        options: [],
        isGroup: true,
        config: {
          optionType: 'button',
          buttonStyle: 'solid',
        },
      },
      {
        field: 'postIds',
        type: 'select',
        label: '岗位',
        options: ref([]),
        config: {
          allowClear: false,
          mode: 'multiple',
          fieldNames: {
            value: 'postId',
            label: 'postName',
          },
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
        field: 'roleIds',
        type: 'select',
        label: '角色',
        options: ref([]),
        config: {
          allowClear: false,
          mode: 'multiple',
          fieldNames: {
            value: 'roleId',
            label: 'roleName',
          },
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
        field: 'remark',
        type: 'textarea',
        label: '备注',
        layout: {
          xl: 24,
          lg: 24,
          md: 24,
          sm: 24,
          xs: 24,
        },
        config: {
          maxlength: 500,
          showCount: true,
        },
      },
    ],
    colLayout: {
      xl: 12,
      lg: 12,
      md: 12,
      sm: 12,
      xs: 24,
    },
    itemStyle: {
      padding: '0px 8px 0px 8px',
    },
    formConfig: {
      layout: 'horizontal',
      labelCol: { style: { width: '80px' } },
      wrapperCol: { flex: 1 },
    },
  }
}
