export const tableItem = [
  {
    prop: 'deptName',
    label: '部门名称',
    minWidth: 160,
  },
  {
    prop: 'orderNum',
    label: '排序',
    minWidth: 100,
  },
  {
    prop: 'status',
    label: '状态',
    slotName: 'statusSlot',
    minWidth: 100,
  },
  {
    prop: 'createTime',
    label: '创建时间',
    minWidth: 160,
  },
  {
    prop: 'todo',
    label: '操作',
    width: 250,
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
      rowKey: 'deptId',
      treeProps: { children: 'children', hasChildren: 'hasChildren' },
      stripe: false,
      defaultExpandAll: true,
    },
    showIndex: false,
    showChoose: true,
    pagination: false,
    // border: false,
  }
}
