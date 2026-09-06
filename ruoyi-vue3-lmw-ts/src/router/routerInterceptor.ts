import { ElMessage } from 'element-plus'
import loading from '@/utils/loading'
import { getToken } from '@/utils/auth'
import { isHttp } from '@/utils/validate'
import useUserStore from '@/store/modules/user.ts'
import usePermissionStore from '@/store/modules/permission'
import { isRelogin } from '@/utils/hsj/service/index.ts'
import NProgress from 'nprogress'
import { Router } from 'vue-router'
import type { RouteLocationNormalized, NavigationGuardNext } from 'vue-router'
NProgress.configure({ showSpinner: false })
const whiteList = ['/login', '/register']
const whiteNext = (next: NavigationGuardNext, to: RouteLocationNormalized) => {
  if (whiteList.indexOf(to.path) !== -1) {
    // 在免登录白名单，直接进入
    next()
  } else {
    next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
  }
}
export const beforeEach = (router: Router) => {
  router.beforeEach((to, _from, next) => {
    NProgress.start()
    if (!window.existLoading) {
      loading.show()
      window.existLoading = true
    }
    if (getToken()) {
      document.title = to.meta.title ?? import.meta.env.VITE_APP_TITLE
      /* has token*/
      if (to.path === '/login') {
        next({ path: '/' })
      } else {
        if (useUserStore().roles.length === 0) {
          isRelogin.show = true
          // 判断当前用户是否已拉取完user_info信息
          useUserStore()
            .getInfo()
            .then(() => {
              isRelogin.show = false
              usePermissionStore()
                .generateRoutes()
                .then((accessRoutes) => {
                  // 根据roles权限生成可访问的路由表
                  accessRoutes.forEach((route) => {
                    if (!isHttp(route.path)) {
                      router.addRoute(route) // 动态添加可访问路由表
                    }
                  })
                  next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
                })
            })
            .catch((err) => {
              useUserStore()
                .logOut()
                .then(() => {
                  if (whiteList.indexOf(to.path) === -1) {
                    ElMessage({
                      message: err,
                      type: 'error',
                      plain: true,
                    })
                  }
                })
                .finally(() => {
                  whiteNext(next, to)
                })
            })
        } else {
          next()
        }
      }
    } else {
      whiteNext(next, to)
    }
  })
}
export const afterEach = (router: Router) => {
  router.afterEach(() => {
    if (window.existLoading) {
      loading.hide()
    }
    NProgress.done()
  })
}
