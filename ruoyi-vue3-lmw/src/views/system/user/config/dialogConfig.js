export default (listeners = {}) => {
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
          clearable: false,
          maxlength: 30,
        },
      },
      {
        label: '归属部门',
        field: 'deptId',
        type: 'treeSelect',
        options: ref([]),
        config: {
          props: { label: 'label', value: 'id', children: 'children' },
          checkStrictly: true,
        },
      },
      {
        field: 'phonenumber',
        type: 'input',
        label: '手机号码',
        config: {
          clearable: false,
          maxlength: 11,
        },
      },
      {
        field: 'email',
        type: 'input',
        label: '邮箱',
        config: {
          clearable: false,
          maxlength: 50,
        },
      },
      {
        field: 'userName',
        type: 'input',
        label: '用户名称',
        config: {
          clearable: false,
          maxlength: 30,
        },
      },

      {
        field: 'password',
        type: 'input',
        label: '用户密码',
        config: {
          clearable: false,
          maxlength: 20,
          showPassword: true,
        },
      },
      {
        field: 'sex',
        type: 'select',
        options: [],
        label: '性别',
        config: {
          clearable: false,
        },
      },

      {
        field: 'status',
        type: 'radio',
        options: [],
        label: '状态',
        isGroup: true,
        config: {
          clearable: false,
        },
        optionConfig: {
          border: true,
        },
      },

      {
        field: 'postIds',
        type: 'select',
        options: [],
        label: '岗位',
        config: {
          clearable: false,
          multiple: true,
        },
        setValue: 'postId',
        setLabel: 'postName',
      },
      {
        field: 'roleIds',
        type: 'select',
        options: [],
        config: {
          clearable: false,
          multiple: true,
        },
        label: '角色',
        setValue: 'roleId',
        setLabel: 'roleName',
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
    elFormConfig: {
      labelWidth: '80px',
    },
    hideItems: ref([]),
  }
}
