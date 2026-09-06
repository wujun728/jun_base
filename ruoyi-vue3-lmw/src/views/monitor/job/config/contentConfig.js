export const tableItem = [
  {
    prop: 'jobName',
    label: '任务名称',
    minWidth: 140,
    mobileSlot: 'header',
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
    mobileSlot: 'footer',
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
      showOverflowTooltip: false,
    },
    showIndex: true,
    showChoose: true,
    pagination: true,
  }
}
