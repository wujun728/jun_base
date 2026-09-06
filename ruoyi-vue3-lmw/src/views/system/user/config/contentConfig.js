export const tableItem = [
  {
    prop: 'userName',
    label: '用户名称',
    minWidth: 90,
    mobileSlot: 'header',
  },
  {
    prop: 'nickName',
    label: '用户昵称',
    minWidth: 90,
  },
  {
    prop: 'dept',
    label: '部门',
    slotName: 'deptSlot',
    minWidth: 120,
  },
  {
    prop: 'phonenumber',
    label: '手机号码',
    minWidth: 120,
  },
  {
    prop: 'status',
    label: '状态',
    minWidth: 120,
    slotName: 'statusSlot',
  },
  {
    prop: 'createTime',
    label: '创建时间',
    minWidth: 180,
  },
  {
    prop: 'todo',
    label: '操作',
    width: 280,
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
      rowKey: 'userId',
    },
    showIndex: true,
    showChoose: true,
    pagination: true,
    selectionConfig: {
      selectable: (item) => {
        return item.userId !== 1
      },
    },
  }
}
