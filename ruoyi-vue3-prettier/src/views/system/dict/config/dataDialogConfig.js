export default (listeners = {}) => {
  return {
    rules: {
      dictLabel: [
        { required: true, message: '数据标签不能为空', trigger: 'blur' },
      ],
      dictValue: [
        { required: true, message: '数据键值不能为空', trigger: 'blur' },
      ],
      dictSort: [
        { required: true, message: '数据顺序不能为空', trigger: 'blur' },
      ],
    },
    formItems: [
      {
        field: 'dictType',
        type: 'input',
        label: '字典类型',
        config: {
          disabled: true,
          maxlength: 30,
        },
      },

      {
        field: 'dictLabel',
        type: 'input',
        label: '数据标签',
        config: {
          maxlength: 30,
        },
      },
      {
        field: 'dictValue',
        type: 'input',
        label: '数据键值',
        config: {
          maxlength: 30,
        },
      },
      {
        field: 'cssClass',
        type: 'input',
        label: '样式属性',
        config: {
          maxlength: 30,
        },
      },

      {
        field: 'dictSort',
        type: 'inputNumber',
        label: '显示排序',
        config: {
          controlsPosition: 'right',
          min: 0,
        },
      },
      {
        field: 'listClass',
        type: 'select',
        label: '回显样式',
        options: [
          { value: 'default', label: '默认' },
          { value: 'primary', label: '主要' },
          { value: 'success', label: '成功' },
          { value: 'info', label: '信息' },
          { value: 'warning', label: '警告' },
          { value: 'danger', label: '危险' },
        ],
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
    hideItems: ref([]),
  }
}
