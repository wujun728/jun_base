export default (otherConfig = {}) => {
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
          maxlength: 30,
        },
      },
      {
        label: '岗位编码',
        field: 'postCode',
        type: 'input',
      },
      {
        field: 'postSort',
        type: 'inputNumber',
        label: '岗位顺序',
        config: {
          controlsPosition: 'right',
          min: 0,
        },
      },
      {
        field: 'status',
        type: 'radio',
        label: '部门状态',
        isGroup: true,
        options: [],
        optionConfig: {
          border: true,
        },
      },
      {
        field: 'remark',
        type: 'textarea',
        label: '备注',
        config: {
          autosize: { minRows: 4, maxRows: 6 },
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
    itemStyle: {
      padding: '0px 8px 0px 8px',
    },
    elFormConfig: {
      labelWidth: '80px',
    },
  }
}
