export const tableItem = [
  {
    prop: 'userId',
    label: '用户编号',
    minWidth: 90,
  },
  {
    prop: 'userName',
    label: '用户名称',
    minWidth: 90,
  },
  {
    prop: 'nickName',
    label: '用户昵称',
    minWidth: 90,
  },
  {
    prop: 'dept',
    slotName: 'deptSlot',
    label: '部门',
    minWidth: 120,
  },
  {
    prop: 'phonenumber',
    label: '手机号码',
    width: 120,
  },
  {
    prop: 'status',
    label: '状态',
    width: 120,
    slotName: 'statusSlot',
  },
  {
    prop: 'createTime',
    label: '创建时间',
    width: 180,
  },
  {
    prop: 'todo',
    label: '操作',
    width: 270,
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
    showIndex: false,
    showChoose: true,
    pagination: true,
    selectionConfig: {
      selectable: (item) => {
        return item.userId !== 1
      },
    },
  }
}
