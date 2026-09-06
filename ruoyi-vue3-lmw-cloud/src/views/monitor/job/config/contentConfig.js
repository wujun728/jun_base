export const tableItem = [
  {
    prop: 'jobId',
    label: '任务编号',
    width: 100,
  },
  {
    prop: 'jobName',
    label: '任务名称',
  },
  {
    prop: 'jobGroup',
    label: '任务组名',
    slotName: 'jobGroupSlot',
  },

  {
    prop: 'invokeTarget',
    label: '调用目标字符串',
  },
  {
    prop: 'cronExpression',
    label: 'cron执行表达式',
  },
  {
    prop: 'status',
    label: '状态',
    slotName: 'statusSlot',
  },
  {
    label: '操作',
    width: '280',
    slotName: 'toSth',
    showOverflowTooltip: false,
  },
]
export default () => {
  return {
    tableItem,
    elTableConfig: {
      tooltipOptions: {
        popperClass: 'lmw_popper',
        effect: 'light',
      },
      rowKey: 'jobId',
    },
    showIndex: false,
    showChoose: true,
    pagination: true,
    // border: false,
  }
}
