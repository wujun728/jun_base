export const tableItem = [
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
    prop: 'email',
    label: '邮箱',
    minWidth: 120,
  },
  {
    prop: 'phonenumber',
    label: '手机',
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
    width: 160,
    fixed: !window.isSmallScreen ? 'right' : undefined,
    slotName: 'todo',
    ellipsis: false,
  },
]

export default () => {
  return {
    tableItem,
    tableConfig: {
      rowKey: 'userId',
    },
    showIndex: false,
    showChoose: true,
    pagination: true,
  }
}
