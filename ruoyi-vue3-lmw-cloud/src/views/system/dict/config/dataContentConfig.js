export const tableItem = [
  {
    prop: 'dictCode',
    label: '字典编码',
  },
  {
    prop: 'dictLabel',
    label: '字典标签',
    slotName: 'dictLabelSlot',
  },
  {
    prop: 'dictValue',
    label: '字典键值',
  },

  {
    prop: 'dictSort',
    label: '字典排序',
  },
  {
    prop: 'status',
    label: '状态',
    slotName: 'statusSlot',
  },

  {
    prop: 'remark',
    label: '备注',
  },
  {
    prop: 'createTime',
    label: '创建时间',
    width: '180',
  },
  {
    prop: 'todo',
    label: '操作',
    width: '200',
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
      rowKey: 'dictCode',
    },
    showIndex: false,
    showChoose: true,
    pagination: true,
    // border: false,
  }
}
