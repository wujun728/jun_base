export const tableItem = [
  {
    prop: 'tableName',
    label: '表名称',
    minWidth: 110,
    mobileSlot: 'header',
  },
  {
    prop: 'tableComment',
    label: '表描述',
    minWidth: 120,
  },
  {
    prop: 'className',
    label: '实体',
    minWidth: 120,
  },
  {
    prop: 'createTime',
    label: '创建时间',
    minWidth: 160,
  },
  {
    prop: 'updateTime',
    label: '更新时间',
    minWidth: 160,
  },
  {
    prop: 'doSth',
    label: '操作',
    width: 280,
    slotName: 'doSth',
    mobileSlot: 'footer',
    ellipsis: false,
  },
]

export default () => {
  return {
    tableItem,
    tableConfig: {
      rowKey: 'tableId',
    },
    showIndex: false,
    showChoose: true,
    pagination: true,
  }
}
