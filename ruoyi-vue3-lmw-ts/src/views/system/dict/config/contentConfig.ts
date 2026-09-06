export const tableItem: BaseTableItem[] = [
  {
    prop: 'dictName',
    label: '字典名称',
    minWidth: 120,
  },
  {
    prop: 'dictType',
    label: '字典类型',
    slotName: 'dictTypeSlot',
    minWidth: 170,
  },

  {
    prop: 'status',
    label: '状态',
    slotName: 'statusSlot',
    minWidth: 90,
  },
  {
    prop: 'remark',
    label: '备注',
    minWidth: 100,
  },
  {
    prop: 'createTime',
    label: '创建时间',
    minWidth: 180,
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
export default (): BaseTableProps => {
  return {
    tableItem,
    elTableConfig: {
      tooltipOptions: {
        popperClass: 'lmw_popper',
        effect: 'light',
      },
      rowKey: 'dictId',
    },
    showIndex: true,
    showChoose: true,
    pagination: true,
  }
}
