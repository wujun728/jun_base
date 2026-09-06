export const tableItem = [
  {
    prop: 'tableName',
    label: '表名称',
  },
  {
    prop: 'tableComment',
    label: '表描述',
  },
  {
    prop: 'className',
    label: '实体',
  },

  {
    prop: 'createTime',
    label: '创建时间',
    width: 160,
  },
  {
    prop: 'updateTime',
    label: '更新时间',
    width: 160,
  },
  {
    prop: 'doSth',
    label: '操作',
    width: '280',
    slotName: 'doSth',
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
    },
    showIndex: false,
    showChoose: true,
    pagination: true,
    // border: false,
  }
}
