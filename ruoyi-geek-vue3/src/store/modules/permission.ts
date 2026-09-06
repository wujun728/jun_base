import auth from '@/plugins/auth'
import { router } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index.vue'
import ParentView from '@/components/ParentView/index.vue'
import InnerLink from '@/layout/components/InnerLink/index.vue'
import { defineStore } from 'pinia'
import type { Component } from 'vue'
import { RouteItem } from '@/types/route'
import { constantRoutes } from '@/router/routes/staticRoutes'
import { dynamicRoutes } from '@/router/routes/asyncRoutes'
import { deepClone } from '@/utils'

// 匹配views里面所有的.vue文件
const modules = import.meta.glob(['../../**/views/**/*.vue', '../../**/view/**/*.vue'])

// 定义 store 状态接口
interface PermissionState {
  routes: RouteItem[]
  addRoutes: RouteItem[]
  defaultRoutes: RouteItem[]
  topbarRouters: RouteItem[]
  sidebarRouters: RouteItem[]
}

/**
 * 权限管理模块
 * 
 * 路由生成说明：
 * 1. generateRoutes方法负责生成所有路由：
 *    - 从后端获取动态路由数据
 *    - 处理动态路由数据（过滤、转换组件等）
 *    - 根据设置决定TopNav菜单的数据来源
 * 
 * 2. TopNav菜单数据生成规则：
 *    - 启用TopNav导入本地路由：constantRoutes + defaultRoutes
 *    - 关闭TopNav导入本地路由：仅使用defaultRoutes
 *    - 通过settingsStore.topNavMixMenu控制
 */

const usePermissionStore = defineStore(
  'permission',
  {
    state: (): PermissionState => ({
      routes: [],
      addRoutes: [],
      defaultRoutes: [],
      topbarRouters: [],
      sidebarRouters: []
    }),
    actions: {
      setRoutes(routes: RouteItem[]) {
        this.routes = [...constantRoutes, ...routes];
      },
      setDefaultRoutes(routes: RouteItem[]) {
        this.defaultRoutes = deepClone(routes);
      },
      setTopbarRoutes(routes: RouteItem[]) {
        this.topbarRouters = deepClone(routes);
      },
      setSidebarRouters(routes: RouteItem[]) {
        this.sidebarRouters = deepClone(routes);
      },
      generateRoutes(): Promise<RouteItem[]> {
        return new Promise(resolve => {
          // 向后端请求路由数据
          getRouters().then(res => {
            const sidebarRoutes = constantRoutes.concat(filterAsyncRouter(deepClone(res.data)))
            const rewriteRoutes = filterAsyncRouter(deepClone(res.data), true)
            const asyncRoutes = filterDynamicRoutes(dynamicRoutes)
            asyncRoutes.forEach(route => { router.addRoute(route) })
            this.setRoutes(rewriteRoutes)
            this.setSidebarRouters(sidebarRoutes)
            this.setDefaultRoutes(sidebarRoutes)
            this.setTopbarRoutes(sidebarRoutes)
            resolve(rewriteRoutes)
          })
        })
      }
    }
  })

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap: RouteItem[], type = false): RouteItem[] {
  return asyncRouterMap.filter(route => {
    // 确保route有hidden属性
    if (route.hidden === undefined) {
      route.hidden = false;
    }

    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (typeof route.component === 'string') {
        if (route.component === 'Layout') {
          route.component = Layout
        } else if (route.component === 'ParentView') {
          route.component = ParentView
        } else if (route.component === 'InnerLink') {
          route.component = InnerLink
        } else {
          route.component = loadView(route.component)
        }
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}
/**
 * 递归过滤并处理路由子项，将所有子路由展平为一个数组，并根据父路由调整路径。
 * 如果遇到组件为 'ParentView' 的路由，会将其子路由的路径拼接父路由路径，并继续递归处理。
 * 
 * @param childrenMap 路由子项数组
 * @param lastRouter 上一级父路由（可选），用于路径拼接
 * @returns 处理后的路由子项数组
 */
function filterChildren(childrenMap: RouteItem[], lastRouter?: RouteItem): RouteItem[] {
  const children: RouteItem[] = []
  childrenMap.forEach((el) => {
    const item = { ...el, hidden: false } // 确保hidden属性存在
    if (el.children && el.children.length) {
      if (el.component === 'ParentView' && !lastRouter) {
        el.children.forEach((c: RouteItem) => {
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {
            children.push(...filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter && lastRouter.path) {
      item.path = lastRouter.path + '/' + item.path
    }
    children.push(item)
  })
  return children
}

// 动态路由遍历，验证是否具备权限
function filterDynamicRoutes(routes: readonly RouteItem[]): RouteItem[] {
  const res: RouteItem[] = []
  routes.forEach(route => {
    if (route.permissions) {
      if (auth.hasPermiOr(route.permissions)) {
        res.push(route)
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        res.push(route)
      }
    }
  })
  return res
}

const loadView = (view: string): (() => Promise<Component>) => {
  let res: (() => Promise<Component>) | undefined
  for (const path in modules) {
    // 要考虑views 或者view 两种情况
    let dir = ''
    if (path.includes('modules/')) {
      dir += path.split('modules/')[1].split('/view')[0]
      dir += "/"
    }
    if (path.includes('views/')) {
      dir += path.split('views/')[1].split('.vue')[0]
    } else if (path.includes('view/')) {
      dir += path.split('view/')[1].split('.vue')[0]
    }
    if (dir === view) {
      res = modules[path] as () => Promise<Component>
    }
  }
  return res || (() => Promise.resolve({} as Component))
}

export default usePermissionStore
