export const tableItem = [
  {
    prop: 'dictCode',
    label: '字典编码',
    minWidth: 90,
  },
  {
    prop: 'dictLabel',
    label: '字典标签',
    slotName: 'dictLabelSlot',
    minWidth: 120,
  },
  {
    prop: 'dictValue',
    label: '字典键值',
    minWidth: 100,
  },
  {
    prop: 'dictSort',
    label: '字典排序',
    minWidth: 90,
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
    minWidth: 120,
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
    fixed: !window.isSmallScreen ? 'right' : undefined,
    slotName: 'todo',
    ellipsis: false,
  },
]

export default () => {
  return {
    tableItem,
    tableConfig: {
      rowKey: 'dictCode',
    },
    showIndex: false,
    showChoose: true,
    pagination: true,
  }
}
