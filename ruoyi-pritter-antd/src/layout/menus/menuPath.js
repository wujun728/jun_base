import { getNormalPath } from '@/utils/utils'

export function resolveMenuPath(basePath, routePath) {
  if (!routePath) {
    return basePath
  }
  if (routePath.startsWith('/')) {
    return getNormalPath(routePath)
  }
  if (!basePath) {
    return getNormalPath(`/${routePath}`)
  }
  return getNormalPath(`${basePath}/${routePath}`)
}

export function resolveSubMenuKey(basePath, itemPath, isNest) {
  if (isNest) {
    return basePath
  }
  return resolveMenuPath(basePath, itemPath)
}

/** 与 MenuTree 中 a-sub-menu 的 key 保持一致 */
export function getMenuSubmenuKey(menuRoute, basePath) {
  if (!basePath) {
    return resolveSubMenuKey(menuRoute.path, menuRoute.path, false)
  }
  return resolveMenuPath(basePath, menuRoute.path)
}
