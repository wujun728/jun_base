export const tableItem = [
  {
    prop: 'operId',
    label: '日志编号',
    minWidth: 100,
  },
  {
    prop: 'title',
    label: '系统模块',
    minWidth: 120,
  },
  {
    prop: 'businessType',
    label: '操作类型',
    slotName: 'businessTypeSlot',
    minWidth: 100,
  },
  {
    prop: 'operName',
    label: '操作人员',
    sorter: true,
    minWidth: 120,
  },
  {
    prop: 'status',
    label: '操作状态',
    slotName: 'statusSlot',
    minWidth: 100,
  },
  {
    prop: 'operIp',
    label: '操作地址',
    minWidth: 160,
  },
  {
    prop: 'operTime',
    label: '操作日期',
    minWidth: 160,
    sorter: true,
  },
  {
    prop: 'costTime',
    label: '消耗时间',
    slotName: 'costTimeSlot',
    sorter: true,
    width: 120,
  },
  {
    prop: 'todo',
    label: '操作',
    width: 120,
    fixed: !window.isSmallScreen ? 'right' : undefined,
    slotName: 'todo',
    ellipsis: false,
  },
]

export default () => {
  return {
    tableItem,
    tableConfig: {
      rowKey: 'operId',
    },
    showIndex: false,
    showChoose: true,
    pagination: true,
  }
}
