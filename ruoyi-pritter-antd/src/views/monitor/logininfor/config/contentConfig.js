export const tableItem = [
  {
    prop: 'infoId',
    label: '日志编号',
    minWidth: 100,
  },
  {
    prop: 'userName',
    label: '用户名称',
    sorter: true,
    minWidth: 100,
  },
  {
    prop: 'ipaddr',
    label: '地址',
    minWidth: 140,
  },
  {
    prop: 'loginLocation',
    label: '登录地点',
    minWidth: 120,
  },
  {
    prop: 'os',
    label: '操作系统',
    minWidth: 140,
  },
  {
    prop: 'browser',
    label: '浏览器',
    minWidth: 120,
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
    minWidth: 150,
  },
  {
    prop: 'loginTime',
    label: '访问时间',
    sorter: true,
    minWidth: 170,
  },
  {
    prop: 'todo',
    label: '操作',
    width: 100,
    fixed: !window.isSmallScreen ? 'right' : false,
    slotName: 'todo',
    ellipsis: false,
  },
]
export default () => {
  return {
    tableItem,
    tableConfig: {
      rowKey: 'infoId',
      showOverflowTooltip: false,
    },
    showIndex: false,
    showChoose: true,
    pagination: true,
  }
}
