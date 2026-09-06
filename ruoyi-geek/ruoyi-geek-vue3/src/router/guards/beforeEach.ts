import type { Router } from 'vue-router'

import { ElMessage } from 'element-plus'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import { isHttp } from '@/utils/validate'
import { isRelogin } from '@/utils/request'
import useUserStore from '@/store/modules/user'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'
import { isPathMatch } from '@/utils/validate'
import { RoutesAlias } from '../routesAlias'

const whiteList: string[] = [RoutesAlias.Login, RoutesAlias.Register]
const isWhiteList = (path: string) => whiteList.some(pattern => isPathMatch(pattern, path))

/**
 * 路由全局前置守卫
 * 处理进度条、获取菜单列表、动态路由注册、404 检查、工作标签页及页面标题设置
 */
export function setupBeforeEachGuard(router: Router): void {
  router.beforeEach((to, from, next) => {
    let title: string = typeof to.meta.title === 'function' ? to.meta.title(to) : to.meta.title ?? ''
    useSettingsStore().setTitle(title)
    NProgress.start()
    if (isWhiteList(to.path)) {
      next()
    } else if (getToken()) {
      if (useUserStore().roles.length === 0) {
        isRelogin.show = true
        // 判断当前用户是否已拉取完user_info信息
        useUserStore().getInfo().then(() => {
          isRelogin.show = false
          usePermissionStore().generateRoutes().then(accessRoutes => {
            // 根据roles权限生成可访问的路由表
            accessRoutes.forEach(route => {
              if (!isHttp(route.path)) {
                router.addRoute(route) // 动态添加可访问路由表
              }
            })
            next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
          })
        }).catch(err => {
          useUserStore().logOut().then(() => {
            ElMessage.error(err.message)
            next({ path: RoutesAlias.Home })
          })
        })
      } else {
        next()
      }
    } else {
      if (to.fullPath) next(`${RoutesAlias.Login}?redirect=${to.fullPath}`)
      else next(RoutesAlias.Login)
    }
  })
}