export const tableItem = [
  {
    prop: 'noticeTitle',
    label: '公告标题',
    minWidth: 160,
  },
  {
    prop: 'noticeType',
    label: '公告类型',
    slotName: 'noticeTypeSlot',
    minWidth: 90,
  },
  {
    prop: 'status',
    label: '状态',
    slotName: 'statusSlot',
    minWidth: 90,
  },
  {
    prop: 'createBy',
    label: '创建者',
    minWidth: 90,
  },
  {
    prop: 'createTime',
    label: '创建时间',
    minWidth: 160,
  },
  {
    prop: 'todo',
    label: '操作',
    width: 200,
    fixed: !window.isSmallScreen ? 'right' : undefined,
    slotName: 'todo',
    ellipsis: false,
  },
]

export default () => {
  return {
    tableItem,
    tableConfig: {
      rowKey: 'noticeId',
    },
    showIndex: true,
    showChoose: true,
    pagination: false,
  }
}
