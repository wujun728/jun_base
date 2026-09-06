export default (otherConfig = {}) => {
  return {
    formItems: [
      {
        label: '部门名称',
        field: 'deptName',
        type: 'input',
      },
      {
        label: '状态',
        field: 'status',
        type: 'select',
        options: [],
      },
    ],
  }
}
