export const tableItem = [
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
      rowKey: 'postId',
    },
    showIndex: true,
    showChoose: true,
    pagination: true,
  }
}
