export default (otherConfig = {}) => {
  return {
    formItems: [
      {
        label: '岗位编码',
        field: 'postCode',
        type: 'input',
      },
      {
        label: '岗位名称',
        field: 'postName',
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
