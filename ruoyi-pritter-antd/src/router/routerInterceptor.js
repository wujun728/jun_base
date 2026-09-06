import modal from '@/plugins/modal'
import loading from '@/utils/loading.js'
import { getToken } from '@/utils/auth'
import { isHttp } from '@/utils/validate'
import useUserStore from '@/store/modules/user.js'
import usePermissionStore from '@/store/modules/permission.js'
import { isRelogin } from '@/utils/service/index.js'
import NProgress from 'nprogress'

NProgress.configure({ showSpinner: false })
const whiteList = ['/login', '/register']

function getWhiteListRedirect(to) {
  if (whiteList.indexOf(to.path) !== -1) {
    return true
  }
  return `/login?redirect=${to.fullPath}`
}

export const beforeEach = (router) => {
  router.beforeEach(async (to) => {
    NProgress.start()
    if (!window.existLoading) {
      loading.show()
      window.existLoading = true
    }
    if (getToken()) {
      document.title = to.meta.title ?? import.meta.env.VITE_APP_TITLE
      /* has token*/
      if (to.path === '/login') {
        return { path: '/' }
      }
      if (useUserStore().roles.length === 0) {
        isRelogin.show = true
        try {
          await useUserStore().getInfo()
          isRelogin.show = false
          const accessRoutes = await usePermissionStore().generateRoutes()
          accessRoutes.forEach((route) => {
            if (!isHttp(route.path)) {
              router.addRoute(route)
            }
          })
          return { ...to, replace: true }
        } catch (err) {
          try {
            await useUserStore().logOut()
            if (whiteList.indexOf(to.path) === -1) {
              modal.msgError(err)
            }
          } catch {
            // logOut 失败时仍继续跳转
          }
          return getWhiteListRedirect(to)
        }
      }
      return true
    }
    return getWhiteListRedirect(to)
  })
}

export const afterEach = (router) => {
  router.afterEach(() => {
    if (window.existLoading) {
      loading.hide()
    }
    NProgress.done()
  })
}
