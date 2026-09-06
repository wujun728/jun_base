export default (): BaseFormProps => {
  return {
    formItems: [
      {
        label: '菜单名称',
        field: 'menuName',
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
