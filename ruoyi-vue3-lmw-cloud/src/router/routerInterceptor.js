import { ElMessage } from 'element-plus'
import loading from '@/utils/loading.js'
import { getToken, removeToken } from '@/utils/auth'
import { isHttp } from '@/utils/validate'
import useUserStore from '@/store/modules/user.js'
import usePermissionStore from '@/store/modules/permission.js'
import { isRelogin } from '@/utils/hsj/service/index.js'
import NProgress from 'nprogress'

NProgress.configure({ showSpinner: false })
const whiteList = ['/login', '/register']
export const beforeEach = (router) => {
  router.beforeEach((to, from, next) => {
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
                  ElMessage({
                    message: err,
                    type: 'error',
                    plain: true,
                  })
                  next({ path: '/' })
                })
            })
        } else {
          next()
        }
      }
    } else {
      // 没有token
      if (whiteList.indexOf(to.path) !== -1) {
        // 在免登录白名单，直接进入
        next()
      } else {
        next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      }
    }
  })
}
export const afterEach = (router) => {
  router.afterEach((to, from) => {
    if (window.existLoading) {
      loading.hide()
    }
    NProgress.done()
  })
}
