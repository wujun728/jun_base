export default () => {
  return {
    formItems: [
      {
        label: '公告标题',
        field: 'noticeTitle',
        type: 'input',
        config: {
          allowClear: true,
        },
      },
      {
        label: '操作人员',
        field: 'createBy',
        type: 'input',
        config: {
          allowClear: true,
        },
      },
      {
        label: '类型',
        field: 'noticeType',
        type: 'select',
        options: [],
        config: {
          allowClear: true,
        },
      },
    ],
    colLayout: {
      xxl: 4,
      xl: 6,
      lg: 8,
      md: 12,
      sm: 12,
      xs: 24,
    },
  }
}
