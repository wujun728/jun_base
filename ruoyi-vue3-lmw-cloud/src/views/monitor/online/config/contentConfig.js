export const tableItem = [
  {
    prop: 'tokenId',
    label: '会话编号',
  },
  {
    prop: 'userName',
    label: '登录名称',
  },
  {
    prop: 'deptName',
    label: '所属部门',
  },

  {
    prop: 'ipaddr',
    label: '主机',
  },
  {
    prop: 'loginLocation',
    label: '登录地点',
  },
  {
    prop: 'os',
    label: '操作系统',
  },
  {
    prop: 'browser',
    label: '浏览器',
  },
  {
    prop: 'loginTime',
    label: '登录时间',
    width: '180',
    slotName: 'loginTimeSlot',
  },
  {
    prop: 'doSth',
    label: '操作',
    width: '100',
    fixed: !window.isSmallScreen ? 'right' : false,
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
      rowKey: 'tokenId',
      showOverflowTooltip: false,
    },
    showIndex: true,
    showChoose: false,
    pagination: true,
    // border: false,
  }
}
