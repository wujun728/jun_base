/**
 * 菜单处理器
 *
 * 负责菜单数据的获取、过滤和处理
 *
 * @module router/core/MenuProcessor
 * @author Art Design Pro Team
 */

import type { AppRouteRecord, RuoYiAppRouteRecord } from '@/types/router'

import { fetchGetMenuList } from '@/api/system-manage'
import { useAppMode } from '@/hooks/core/useAppMode'
import { useUserStore } from '@/store/modules/user'
import { formatMenuTitle } from '@/utils'

import { asyncRoutes } from '../routes/asyncRoutes'
import { RoutesAlias } from '../routesAlias'

export class MenuProcessor {
  /**
   * 获取菜单数据
   */
  async getMenuList(): Promise<AppRouteRecord[]> {
    const { isFrontendMode } = useAppMode()

    let menuList: AppRouteRecord[]
    if (isFrontendMode.value) {
      menuList = await this.processFrontendMenu()
    } else {
      menuList = await this.processBackendMenu()
    }

    // 在规范化路径之前，验证原始路径配置
    this.validateMenuPaths(menuList)

    // 规范化路径（将相对路径转换为完整路径）
    return this.normalizeMenuPaths(menuList)
  }

  /**
   * 处理前端控制模式的菜单
   */
  private async processFrontendMenu(): Promise<AppRouteRecord[]> {
    const userStore = useUserStore()
    const roles = userStore.roles

    // let menuList = [...asyncRoutes]

    let menuList = this.convertToArtDesignProMenu([...asyncRoutes])
    // 根据角色过滤菜单
    if (roles && roles.length > 0) {
      menuList = this.filterMenuByRoles(menuList, roles)
    }

    return this.filterEmptyMenus(menuList)
  }

  /**
   * 处理后端控制模式的菜单
   */
  private async processBackendMenu(): Promise<AppRouteRecord[]> {
    let menuList = await fetchGetMenuList()
    menuList = [...asyncRoutes, ...menuList]
    return this.filterEmptyMenus(this.convertToArtDesignProMenu(menuList))
  }

  /**
   * 若依菜单转Art Design Pro菜单 & 根据用户权限过滤
   */
  convertToArtDesignProMenu(menuList: RuoYiAppRouteRecord[]): AppRouteRecord[] {
    let AppRouteRecordList: AppRouteRecord[] = []

    for (const item of menuList) {
      // 处理外链
      let link: string | undefined = undefined
      if (item.path.startsWith('http')) {
        link = item.path
      }
      // 处理布局组件
      if (item?.component === 'Layout') {
        item.component = 'layout/index'
      } else if (item?.component === 'ParentView') {
        item.component = ''
      }

      const appRoute: AppRouteRecord = {
        name: item.name,
        path: item.path,
        component: item?.component,
        meta: {
          title: item.meta?.title || '',
          icon: item.meta?.icon || '',
          /** 是否在菜单中隐藏 */
          isHide: item.hidden || false,
          /** 是否在标签页中隐藏 */
          isHideTab: item.hidden || false,
          /** 角色权限 */
          roles: item.roles || [],
          /** 是否缓存 */
          keepAlive: !item.meta?.noCache || false,
          /** 是否固定在顶部 */
          fixed: item.meta?.affix || false,
          /** 外链 */
          link: link,
          /** 是否为iframe */
          permissions: item.permissions || [],
          /** 激活显示路径 */
          activePath: item.meta?.activeMenu || ''
        }
        // /** 是否在菜单中显示 */
        // alwaysShow?: boolean
        // /** 菜单权限 */
        // permissions?: string[]
        // meta: {
        //   breadcrumb?: boolean // 如果设置为false，则不会在breadcrumb面包屑中显示
        //   activeMenu?: string // 当路由设置了该属性，则会高亮相对应的侧边栏。
        // }
      }
      // 判断用户角色
      const isAuth =
        item.permissions?.some((auth) => this.hasAuth(auth)) || true
      console.log('isAuth', isAuth)
      if (isAuth) {
        const { children } = item
        if (children) {
          appRoute.children = this.convertToArtDesignProMenu(children)

          if (item.path === '/' && item.children?.length) {
            AppRouteRecordList.push(...appRoute.children)
            continue
          }
        }

        AppRouteRecordList.push(appRoute)
      }
    }

    return AppRouteRecordList
  }

  /**
   * 根据角色过滤菜单
   */
  private filterMenuByRoles(
    menu: AppRouteRecord[],
    roles: string[]
  ): AppRouteRecord[] {
    return menu.reduce((acc: AppRouteRecord[], item) => {
      const itemRoles = item.meta?.roles
      const hasPermission =
        !itemRoles || itemRoles.some((role) => roles?.includes(role))

      if (hasPermission) {
        const filteredItem = { ...item }
        if (filteredItem.children?.length) {
          filteredItem.children = this.filterMenuByRoles(
            filteredItem.children,
            roles
          )
        }
        acc.push(filteredItem)
      }

      return acc
    }, [])
  }

  /**
   * 递归过滤空菜单项
   */
  private filterEmptyMenus(menuList: AppRouteRecord[]): AppRouteRecord[] {
    return menuList
      .map((item) => {
        // 如果有子菜单，先递归过滤子菜单
        if (item.children && item.children.length > 0) {
          const filteredChildren = this.filterEmptyMenus(item.children)
          return {
            ...item,
            children: filteredChildren
          }
        }
        return item
      })
      .filter((item) => {
        // 如果定义了 children 属性（即使是空数组），说明这是一个目录菜单，应该保留
        if ('children' in item) {
          return true
        }

        // 如果有外链或 iframe，保留
        if (item.meta?.isIframe === true || item.meta?.link) {
          return true
        }

        // 如果有有效的 component，保留
        if (
          item.component &&
          item.component !== '' &&
          item.component !== RoutesAlias.Layout
        ) {
          return true
        }

        // 其他情况过滤掉
        return false
      })
  }

  /**
   * 验证菜单列表是否有效
   */
  validateMenuList(menuList: AppRouteRecord[]): boolean {
    return Array.isArray(menuList) && menuList.length > 0
  }

  /**
   * 规范化菜单路径
   * 将相对路径转换为完整路径，确保菜单跳转正确
   */
  private normalizeMenuPaths(
    menuList: AppRouteRecord[],
    parentPath = ''
  ): AppRouteRecord[] {
    return menuList.map((item) => {
      // 构建完整路径
      const fullPath = this.buildFullPath(item.path || '', parentPath)

      // 递归处理子菜单
      const children = item.children?.length
        ? this.normalizeMenuPaths(item.children, fullPath)
        : item.children

      const redirect = item.redirect || this.resolveDefaultRedirect(children)

      return {
        ...item,
        path: fullPath,
        redirect,
        children
      }
    })
  }

  /**
   * 为目录型菜单推导默认跳转地址
   */
  private resolveDefaultRedirect(
    children?: AppRouteRecord[]
  ): string | undefined {
    if (!children?.length) {
      return undefined
    }

    for (const child of children) {
      if (this.isNavigableRoute(child)) {
        return child.path
      }

      const nestedRedirect = this.resolveDefaultRedirect(child.children)
      if (nestedRedirect) {
        return nestedRedirect
      }
    }

    return undefined
  }

  /**
   * 判断子路由是否可以作为默认落点
   */
  private isNavigableRoute(route: AppRouteRecord): boolean {
    return Boolean(
      route.path &&
      route.path !== '/' &&
      !route.meta?.link &&
      route.meta?.isIframe !== true &&
      route.component &&
      route.component !== ''
    )
  }

  /**
   * 验证菜单路径配置
   * 检测非一级菜单是否错误使用了 / 开头的路径
   */
  /**
   * 验证菜单路径配置
   * 检测非一级菜单是否错误使用了 / 开头的路径
   */
  private validateMenuPaths(menuList: AppRouteRecord[], level = 1): void {
    menuList.forEach((route) => {
      if (!route.children?.length) return

      const parentName = String(route.name || route.path || '未知路由')

      route.children.forEach((child) => {
        const childPath = child.path || ''

        // 跳过合法的绝对路径：外部链接和 iframe 路由
        if (this.isValidAbsolutePath(childPath)) return

        // 检测非法的绝对路径
        if (childPath.startsWith('/')) {
          this.logPathError(child, childPath, parentName, level)
        }
      })

      // 递归检查更深层级的子路由
      this.validateMenuPaths(route.children, level + 1)
    })
  }

  /**
   * 判断是否为合法的绝对路径
   */
  private isValidAbsolutePath(path: string): boolean {
    return (
      path.startsWith('http://') ||
      path.startsWith('https://') ||
      path.startsWith('/outside/iframe/')
    )
  }

  /**
   * 输出路径配置错误日志
   */
  private logPathError(
    route: AppRouteRecord,
    path: string,
    parentName: string,
    level: number
  ): void {
    const routeName = String(route.name || path || '未知路由')
    const menuTitle = route.meta?.title || routeName
    const suggestedPath = path.split('/').pop() || path.slice(1)

    console.error(
      `[路由配置错误] 菜单 "${formatMenuTitle(menuTitle)}" (name: ${routeName}, path: ${path}) 配置错误\n` +
        `  位置: ${parentName} > ${routeName}\n` +
        `  问题: ${level + 1}级菜单的 path 不能以 / 开头\n` +
        `  当前配置: path: '${path}'\n` +
        `  应该改为: path: '${suggestedPath}'`
    )
  }

  /**
   * 构建完整路径
   */
  private buildFullPath(path: string, parentPath: string): string {
    if (!path) return ''

    // 外部链接直接返回
    if (path.startsWith('http://') || path.startsWith('https://')) {
      return path
    }

    // 如果已经是绝对路径，直接返回
    if (path.startsWith('/')) {
      return path
    }

    // 拼接父路径和当前路径
    if (parentPath) {
      // 移除父路径末尾的斜杠，移除子路径开头的斜杠，然后拼接
      const cleanParent = parentPath.replace(/\/$/, '')
      const cleanChild = path.replace(/^\//, '')
      return `${cleanParent}/${cleanChild}`
    }

    // 没有父路径，添加前导斜杠
    return `/${path}`
  }

  private hasAuth(auth: string): boolean {
    const allPermissions = '*:*:*'
    const { permissions } = toRefs(useUserStore())
    return permissions.value.some(
      (item) => item === allPermissions || item === auth
    )
  }
}
