export const tableItem = [
  {
    prop: 'configName',
    label: '参数名称',
    minWidth: 160,
    mobileSlot: 'header',
  },
  {
    prop: 'configKey',
    label: '参数键名',
    minWidth: 160,
  },
  {
    prop: 'configValue',
    label: '参数键值',
    minWidth: 120,
  },
  {
    prop: 'configType',
    label: '系统内置',
    slotName: 'configTypeSlot',
    minWidth: 90,
  },
  {
    prop: 'remark',
    label: '备注',
    minWidth: 200,
  },
  {
    prop: 'createTime',
    label: '创建时间',
    minWidth: 160,
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
export default () => {
  return {
    tableItem,
    elTableConfig: {
      tooltipOptions: {
        popperClass: 'lmw_popper',
        effect: 'light',
      },
      rowKey: 'configId',
      showOverflowTooltip: false,
    },
    showIndex: true,
    showChoose: true,
    pagination: true,
  }
}
