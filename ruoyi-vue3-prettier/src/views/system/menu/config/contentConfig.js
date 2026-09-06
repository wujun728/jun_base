export const tableItem = [
  {
    prop: 'menuName',
    label: '菜单名称',
    width: 140,
    align: 'left',
  },
  {
    prop: 'icon',
    label: '图标',
    width: 70,
    slotName: 'iconSlot',
  },
  {
    prop: 'orderNum',
    width: 70,
    label: '排序',
  },

  {
    prop: 'perms',
    label: '权限标识',
    width: 180,
  },
  {
    prop: 'component',
    label: '组件路径',
    showOverflowTooltip: true,
    minWidth: 180,
  },
  {
    prop: 'createTime',
    label: '创建时间',
    width: '180',
  },
  {
    prop: 'todo',
    label: '操作',
    width: '260',
    fixed: !window.isSmallScreen ? 'right' : false,
    slotName: 'todo',
    showOverflowTooltip: false,
  },
]
export default () => {
  return {
    tableItem: tableItem,
    elTableConfig: {
      tooltipOptions: {
        popperClass: 'lmw_popper',
        effect: 'light',
      },
      rowKey: 'menuId',
      treeProps: { children: 'children', hasChildren: 'hasChildren' },
      stripe: false,
    },
    showIndex: false,
    showChoose: true,
    pagination: false,
  }
}

// export default (otherConfig) => {
//   return {
//     tableItem: [
//       {
//         key: 'selection',
//         align: 'center',
//         width: 50,
//         cellRenderer: otherConfig.selectionCell,
//         headerCellRenderer: otherConfig.selectionHeader,
//       },
//       {
//         key: 'menuName',
//         title: '菜单名称',
//         dataKey: 'menuName',
//         width: 180,
//         align: 'left',
//       },
//       {
//         key: 'icon',
//         title: '图标',
//         width: 80,
//         align: 'center',
//         cellRenderer: otherConfig.iconCell,
//       },
//       {
//         key: 'orderNum',
//         title: '排序',
//         dataKey: 'orderNum',
//         width: 80,
//         align: 'center',
//       },
//       {
//         key: 'perms',
//         title: '权限标识',
//         dataKey: 'perms',
//         width: 180,
//         align: 'center',
//       },
//       {
//         key: 'component',
//         title: '组件路径',
//         dataKey: 'component',
//         align: 'center',
//         width: 220,
//       },
//       {
//         key: 'createTime',
//         title: '创建时间',
//         dataKey: 'createTime',
//         align: 'center',
//         width: 180,
//       },
//       {
//         key: 'todo',
//         title: '操作',
//         align: 'center',
//         width: 260,
//         cellRenderer: otherConfig.todoCell,
//         fixed: 'right',
//       },
//     ],
//     elTableConfig: {
//       // expandColumnKey: 'menuName',
//       rowKey: 'menuId',
//       // height: 256,
//       // fixed: false,
//     },
//     pagination: false,
//   }
// }
