import { RouteRecordRaw } from 'vue-router'
import auth from '@/plugins/auth'
import router, { constantRoutes, dynamicRoutes } from '@/router/index'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index.vue'
import ParentView from '@/components/ParentView/index.vue'
import InnerLink from '@/layout/components/InnerLink/index.vue'

// 匹配views里面所有的.vue文件
const modules = import.meta.glob('./../../views/**/*.vue')

const usePermissionStore = defineStore('permission', {
  state: (): PermiState => {
    return {
      routes: [],
      addRoutes: [],
      defaultRoutes: [],
      topbarRouters: [],
      sidebarRouters: [],
    }
  },
  actions: {
    setRoutes(routes: RouteRecordRaw[]) {
      this.addRoutes = routes
      this.routes = constantRoutes.concat(routes)
    },
    setDefaultRoutes(routes: RouteRecordRaw[]) {
      this.defaultRoutes = constantRoutes.concat(routes)
    },
    setTopbarRoutes(routes: RouteRecordRaw[]) {
      this.topbarRouters = routes
    },
    setSidebarRouters(routes: RouteRecordRaw[]) {
      this.sidebarRouters = routes
    },
    generateRoutes() {
      return new Promise<RouteRecordRaw[]>((resolve) => {
        // 向后端请求路由数据
        getRouters().then((res) => {
          const sdata: RouteRecordRaw[] = JSON.parse(JSON.stringify(res.data))
          const rdata: RouteRecordRaw[] = JSON.parse(JSON.stringify(res.data))
          const defaultData: RouteRecordRaw[] = JSON.parse(
            JSON.stringify(res.data)
          )
          const sidebarRoutes = filterAsyncRouter(sdata)
          const rewriteRoutes = filterAsyncRouter(rdata, true)
          const defaultRoutes = filterAsyncRouter(defaultData)
          const asyncRoutes = filterDynamicRoutes(dynamicRoutes)
          asyncRoutes.forEach((route) => {
            router.addRoute(route)
          })
          this.setRoutes(rewriteRoutes)
          this.setSidebarRouters(constantRoutes.concat(sidebarRoutes))
          this.setDefaultRoutes(sidebarRoutes)
          this.setTopbarRoutes(defaultRoutes)
          resolve(rewriteRoutes)
        })
      })
    },
  },
})

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap: RouteRecordRaw[], type = false) {
  return asyncRouterMap.filter((route) => {
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // @ts-ignore
      if (route.component === 'Layout') {
        // @ts-ignore
        route.component = Layout
        // @ts-ignore
      } else if (route.component === 'ParentView') {
        // @ts-ignore
        route.component = ParentView
        // @ts-ignore
      } else if (route.component === 'InnerLink') {
        // @ts-ignore
        route.component = InnerLink
      } else {
        // @ts-ignore
        route.component = loadView(route.component)
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

function filterChildren(
  childrenMap: RouteRecordRaw[],
  lastRouter?: RouteRecordRaw
) {
  let children: RouteRecordRaw[] = []
  childrenMap.forEach((el) => {
    if (el.children && el.children.length) {
      // @ts-ignore
      if (el.component === 'ParentView' && !lastRouter) {
        // @ts-ignore
        el.children.forEach((c) => {
          // @ts-ignore
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = lastRouter.path + '/' + el.path
      if (el.children && el.children.length) {
        children = children.concat(filterChildren(el.children, el))
        return
      }
    }
    children = children.concat(el)
  })
  return children
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes: RouteRecordRaw[]) {
  const res: RouteRecordRaw[] = []
  routes.forEach((route) => {
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

export const loadView = (view: string) => {
  let res
  for (const path in modules) {
    const dir = path.split('views/')[1].split('.vue')[0]
    if (dir === view) {
      res = () => modules[path]()
    }
  }
  return res
}

export default usePermissionStore
