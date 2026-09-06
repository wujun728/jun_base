/**
 * 寻找当前路由在顶栏菜单中的数据
 */
export const currentRouteTopActivity = (name, menus) => {
  for (let i = 0; i < menus.length; i++) {
    const item = menus[i]
    // 找到目标
    if (item.name == name) return item
    // 从子级继续寻找
    if (item.children && item.children.length > 0) {
      const find = currentRouteTopActivity(name, item.children)
      if (find) return item
    }
  }
  return false
}

export function findFirstVisibleChild(nodes) {
  let path = ''
  let menu
  function find(nodes) {
    for (let node of nodes) {
      if (!node.hidden) {
        if (node.children && node.children.length > 0) {
          flag = true
          path += '/' + node.path
          menu = node
          let visibleChild = find(node.children)
          if (visibleChild) {
            return visibleChild
          }
        } else {
          path += '/' + node.path
          return node
        }
      } else {
        if (menu) {
          return menu
        }
      }
    }
  }
  const firstMenu = find(nodes)

  return { firstMenu, path }
}
