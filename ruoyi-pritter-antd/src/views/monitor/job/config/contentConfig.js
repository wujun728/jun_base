export const tableItem = [
  {
    prop: 'jobName',
    label: '任务名称',
    minWidth: 140,
  },
  {
    prop: 'jobGroup',
    label: '任务组名',
    slotName: 'jobGroupSlot',
    minWidth: 100,
  },
  {
    prop: 'invokeTarget',
    label: '调用目标字符串',
    minWidth: 160,
  },
  {
    prop: 'cronExpression',
    label: 'cron执行表达式',
    minWidth: 160,
  },
  {
    prop: 'status',
    label: '状态',
    slotName: 'statusSlot',
    minWidth: 100,
  },
  {
    prop: 'doSth',
    label: '操作',
    width: 280,
    slotName: 'toSth',
    ellipsis: false,
  },
]

export default () => {
  return {
    tableItem,
    tableConfig: {
      rowKey: 'jobId',
    },
    showIndex: true,
    showChoose: true,
    pagination: true,
  }
}
