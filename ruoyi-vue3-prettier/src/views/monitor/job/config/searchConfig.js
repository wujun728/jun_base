export default (otherConfig = {}) => {
  return {
    formItems: [
      {
        label: '任务名称',
        field: 'jobName',
        type: 'input',
      },
      {
        label: '任务组名',
        field: 'jobGroup',
        type: 'select',
        options: [],
      },
      {
        label: '任务状态',
        field: 'status',
        type: 'select',
        options: [],
      },
    ],
  }
}
