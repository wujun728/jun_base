export const tableItem: BaseTableItem[] = [
  {
    prop: 'menuName',
    label: '菜单名称',
    width: 140,
    align: 'left',
  },
  {
    prop: 'icon',
    label: '图标',
    width: 70,
    slotName: 'iconSlot',
  },
  {
    prop: 'orderNum',
    width: 70,
    label: '排序',
  },

  {
    prop: 'perms',
    label: '权限标识',
    width: 180,
  },
  {
    prop: 'component',
    label: '组件路径',
    showOverflowTooltip: true,
    minWidth: 180,
  },
  {
    prop: 'createTime',
    label: '创建时间',
    width: '180',
  },
  {
    prop: 'todo',
    label: '操作',
    width: '260',
    fixed: !window.isSmallScreen ? 'right' : false,
    slotName: 'todo',
    showOverflowTooltip: false,
  },
]
export default (): BaseTableProps => {
  return {
    tableItem: tableItem,
    elTableConfig: {
      tooltipOptions: {
        popperClass: 'lmw_popper',
        effect: 'light',
      },
      rowKey: 'menuId',
      treeProps: { children: 'children', hasChildren: 'hasChildren' },
      stripe: false,
    },
    showIndex: false,
    showChoose: true,
    pagination: false,
  }
}
