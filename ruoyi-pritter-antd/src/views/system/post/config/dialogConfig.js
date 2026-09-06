export default () => {
  return {
    rules: {
      postName: [
        { required: true, message: '岗位名称不能为空', trigger: 'blur' },
      ],
      postCode: [
        { required: true, message: '岗位编码不能为空', trigger: 'blur' },
      ],
      postSort: [
        { required: true, message: '岗位顺序不能为空', trigger: 'blur' },
      ],
    },
    formItems: [
      {
        field: 'postName',
        type: 'input',
        label: '岗位名称',
        config: {
          allowClear: false,
          maxlength: 30,
        },
      },
      {
        label: '岗位编码',
        field: 'postCode',
        type: 'input',
        config: {
          allowClear: false,
          maxlength: 30,
        },
      },
      {
        field: 'postSort',
        type: 'inputNumber',
        label: '岗位顺序',
        config: {
          min: 0,
        },
      },
      {
        field: 'status',
        type: 'radio',
        label: '岗位状态',
        isGroup: true,
        options: [],
        config: {
          optionType: 'button',
          buttonStyle: 'solid',
        },
      },
      {
        field: 'remark',
        type: 'textarea',
        label: '备注',
        config: {
          autoSize: { minRows: 4, maxRows: 6 },
          maxlength: 500,
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
