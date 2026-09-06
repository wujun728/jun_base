export const tableItem = [
  {
    prop: 'infoId',
    label: '日志编号',
    minWidth: 100,
  },
  {
    prop: 'userName',
    label: '用户名称',
    sortable: true,
    minWidth: 120,
    mobileSlot: 'header',
  },
  {
    prop: 'ipaddr',
    label: '地址',
    minWidth: 120,
  },
  {
    prop: 'loginLocation',
    label: '登录地点',
    minWidth: 120,
  },
  {
    prop: 'os',
    label: '操作系统',
    minWidth: 100,
  },
  {
    prop: 'browser',
    label: '浏览器',
    minWidth: 100,
  },
  {
    prop: 'status',
    label: '登录状态',
    slotName: 'statusSlot',
    minWidth: 100,
  },
  {
    prop: 'msg',
    label: '描述',
    minWidth: 100,
  },
  {
    prop: 'loginTime',
    label: '访问时间',
    sortable: true,
    minWidth: 160,
  },
  {
    prop: 'todo',
    label: '操作',
    width: '100',
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
      rowKey: 'infoId',
      showOverflowTooltip: false,
    },
    showIndex: false,
    showChoose: true,
    pagination: true,
  }
}
