export default () => {
  return {
    rules: {
      dictName: [
        { required: true, message: '字典名称不能为空', trigger: 'blur' },
      ],
      dictType: [
        { required: true, message: '字典类型不能为空', trigger: 'blur' },
      ],
    },
    formItems: [
      {
        field: 'dictName',
        type: 'input',
        label: '字典名称',
        config: {
          allowClear: false,
          maxlength: 30,
        },
      },
      {
        field: 'dictType',
        type: 'input',
        label: '字典类型',
        config: {
          allowClear: false,
          maxlength: 100,
        },
      },
      {
        field: 'status',
        type: 'radio',
        label: '状态',
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
