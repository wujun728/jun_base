export default (listeners = {}) => {
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
