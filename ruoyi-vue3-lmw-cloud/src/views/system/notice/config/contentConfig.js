export const tableItem = [
  {
    prop: 'noticeId',
    label: '序号',
    minWidth: 90,
  },
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
    fixed: !window.isSmallScreen ? 'right' : false,
    slotName: 'todo',
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
      stripe: false,
      rowKey: 'noticeId',
      showOverflowTooltip: false,
    },
    showIndex: false,
    showChoose: true,
    pagination: false,
    // border: false,
  }
}
