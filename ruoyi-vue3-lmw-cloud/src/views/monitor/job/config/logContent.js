export const tableItem = [
  {
    prop: 'jobLogId',
    label: '日志编号',
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
    prop: 'jobMessage',
    label: '日志信息',
  },
  {
    prop: 'status',
    label: '执行状态',
    slotName: 'statusSlot',
  },
  {
    prop: 'createTime',
    label: '执行时间',
    width: 180,
    slotName: 'createTimeSlot',
  },
  {
    label: '操作',
    width: '100',
    fixed: !window.isSmallScreen ? 'right' : false,
    slotName: 'doSth',
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
      rowKey: 'logId',
    },
    showIndex: false,
    showChoose: true,
    pagination: true,
    // border: false,
  }
}
