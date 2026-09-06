export const tableItem: BaseTableItem[] = [
  {
    prop: 'jobId',
    label: '任务编号',
    minWidth: 100,
  },
  {
    prop: 'jobName',
    label: '任务名称',
    minWidth: 100,
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
    minWidth: 140,
  },
  {
    prop: 'cronExpression',
    label: 'cron执行表达式',
    minWidth: 140,
  },
  {
    prop: 'status',
    label: '状态',
    slotName: 'statusSlot',
    minWidth: 100,
  },
  {
    prop: 'toSth',
    label: '操作',
    width: 280,
    slotName: 'toSth',
  },
]
export default (): BaseTableProps => {
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
    showIndex: false,
    showChoose: true,
    pagination: true,
    // border: false,
  }
}
