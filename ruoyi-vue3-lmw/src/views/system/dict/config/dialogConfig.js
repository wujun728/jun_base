export default (listeners = {}) => {
  return {
    formItems: [
      {
        field: 'dictName',
        type: 'input',
        label: '字典名称',
        config: {
          maxlength: 30,
        },
      },
      {
        field: 'dictType',
        type: 'input',
        label: '字典类型',
      },
      {
        field: 'status',
        type: 'radio',
        label: '状态',
        isGroup: true,
        options: [],
        config: {
          clearable: false,
        },
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
