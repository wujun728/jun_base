export default (listeners = {}) => {
  return {
    itemStyle: { padding: '0px 0px 0px 0px' },
    rules: {
      configName: [
        { required: true, message: '参数名称不能为空', trigger: 'blur' },
      ],
      configKey: [
        { required: true, message: '参数键名不能为空', trigger: 'blur' },
      ],
      configValue: [
        { required: true, message: '参数键值不能为空', trigger: 'blur' },
      ],
    },
    formItems: [
      {
        field: 'configName',
        type: 'input',
        label: '参数名称',
      },
      {
        field: 'configKey',
        type: 'input',
        label: '参数键名',
      },
      {
        field: 'configValue',
        type: 'input',
        label: '参数键值',
      },
      {
        field: 'configType',
        type: 'select',
        options: [],
        label: '系统内置',
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
    elFormConfig: {
      labelWidth: '80px',
    },
  }
}
