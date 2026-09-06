export default () => {
  return {
    itemStyle: { padding: '0px 0px 0px 0px' },
    rules: {
      noticeTitle: [
        { required: true, message: '公告标题不能为空', trigger: 'blur' },
      ],
      noticeType: [
        { required: true, message: '公告类型不能为空', trigger: 'change' },
      ],
    },
    formItems: [
      {
        field: 'noticeTitle',
        type: 'input',
        label: '公告标题',
        config: {
          allowClear: false,
          maxlength: 50,
        },
      },
      {
        field: 'noticeType',
        type: 'select',
        options: [],
        label: '公告类型',
        layout: {
          xl: 12,
          lg: 12,
          md: 12,
          sm: 12,
          xs: 24,
        },
        config: {
          allowClear: false,
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
        layout: {
          xl: 12,
          lg: 12,
          md: 12,
          sm: 12,
          xs: 24,
        },
      },
      {
        field: 'noticeContent',
        type: 'custom',
        label: '内容',
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
