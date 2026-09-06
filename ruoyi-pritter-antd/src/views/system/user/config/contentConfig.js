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
    slotName: 'statusSlot',
    minWidth: 120,
  },
  {
    prop: 'createTime',
    label: '创建时间',
    minWidth: 180,
  },
  {
    prop: 'todo',
    label: '操作',
    width: 270,
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
    showIndex: true,
    showChoose: true,
    pagination: true,
    selectionConfig: {
      getCheckboxProps: (record) => ({
        disabled: record.userId === 1,
      }),
    },
  }
}
