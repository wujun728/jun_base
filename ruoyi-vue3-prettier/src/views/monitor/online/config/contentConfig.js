export const tableItem = [
  {
    prop: 'tokenId',
    label: '会话编号',
    minWidth: 160,
    mobileSlot: 'header',
  },
  {
    prop: 'userName',
    label: '登录名称',
    minWidth: 100,
  },
  {
    prop: 'deptName',
    label: '所属部门',
    minWidth: 100,
  },

  {
    prop: 'ipaddr',
    label: '主机',
    minWidth: 90,
  },
  {
    prop: 'loginLocation',
    label: '登录地点',
    minWidth: 100,
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
    prop: 'loginTime',
    label: '登录时间',
    minWidth: 180,
    slotName: 'loginTimeSlot',
  },
  {
    prop: 'doSth',
    label: '操作',
    width: 100,
    fixed: !window.isSmallScreen ? 'right' : false,
    slotName: 'doSth',
    showOverflowTooltip: false,
    mobileSlot: 'footer',
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
      rowKey: 'tokenId',
      showOverflowTooltip: false,
    },
    showIndex: true,
    showChoose: false,
    pagination: true,
    // border: false,
  }
}
