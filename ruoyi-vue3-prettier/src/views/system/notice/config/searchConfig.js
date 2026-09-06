export default (otherConfig = {}) => {
  return {
    formItems: [
      {
        label: '公告标题',
        field: 'noticeTitle',
        type: 'input',
      },
      {
        label: '操作人员',
        field: 'createBy',
        type: 'input',
      },
      {
        label: '类型',
        field: 'noticeType',
        type: 'select',
        options: [],
      },
    ],
  }
}
