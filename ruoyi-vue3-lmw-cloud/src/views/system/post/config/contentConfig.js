export const tableItem = [
  {
    prop: 'postId',
    label: '岗位编号',
    minWidth: 90,
  },
  {
    prop: 'postCode',
    label: '岗位编码',
    minWidth: 90,
  },
  {
    prop: 'postName',
    label: '岗位名称',
    minWidth: 100,
  },

  {
    prop: 'postSort',
    label: '岗位排序',
    minWidth: 90,
  },
  {
    prop: 'status',
    label: '状态',
    minWidth: 120,
    isDict: true,
    slotName: 'status',
  },
  {
    prop: 'createTime',
    label: '创建时间',
    width: 180,
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
      rowKey: 'postId',
    },
    showIndex: false,
    showChoose: true,
    pagination: true,
    // border: false,
  }
}
