export const tableItem = [
  {
    prop: 'roleName',
    label: '角色名称',
    minWidth: 90,
  },
  {
    prop: 'roleKey',
    label: '权限字符',
    minWidth: 120,
  },
  {
    prop: 'roleSort',
    label: '显示顺序',
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
    minWidth: 180,
  },
  {
    prop: 'todo',
    label: '操作',
    width: 220,
    fixed: !window.isSmallScreen ? 'right' : undefined,
    slotName: 'todo',
    ellipsis: false,
  },
]

export default () => {
  return {
    tableItem,
    tableConfig: {
      rowKey: 'roleId',
    },
    showIndex: true,
    showChoose: true,
    pagination: true,
    selectionConfig: {
      getCheckboxProps: (record) => ({
        disabled: record.roleId === 1,
      }),
    },
  }
}
