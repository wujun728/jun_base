export const tableItem: BaseTableItem[] = [
  {
    prop: 'jobLogId',
    label: '日志编号',
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
    prop: 'jobMessage',
    label: '日志信息',
    minWidth: 140,
  },
  {
    prop: 'status',
    label: '执行状态',
    slotName: 'statusSlot',
    minWidth: 100,
  },
  {
    prop: 'createTime',
    label: '执行时间',
    minWidth: 180,
  },
  {
    prop: 'doSth',
    label: '操作',
    width: '100',
    fixed: !window.isSmallScreen ? 'right' : false,
    slotName: 'doSth',
    showOverflowTooltip: false,
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
      rowKey: 'logId',
    },
    showIndex: false,
    showChoose: true,
    pagination: true,
    // border: false,
  }
}
