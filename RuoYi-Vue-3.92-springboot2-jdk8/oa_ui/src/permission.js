// 路由守卫
import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import getPageTitle from '@/utils/get-page-title'
import { roterPre } from '@/settings'
import { getMenus } from '@/utils/auth'
import { AiEmbeddedManage } from "@/libs/ai-embedded"
import { appConfigApi } from '@/api/public'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = [`login`, `share`, '/auth-redirect', '404'] // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  // start progress bar
  NProgress.start()
  // 设置page title
  document.title = getPageTitle(to.meta.title)
  const hasToken = store.getters.token
  
  if (hasToken) {
    const isHideAiModule = [
      to.path.startsWith(`${roterPre}/chat/`),
      to.path.startsWith(`${roterPre}/setting/uploadPicture`),
      to.path.startsWith(`${roterPre}/setting/icons`),
    ].some(flag => flag);

    if (to.path === `${roterPre}/login`) {
      next({ path: '/' })
    } else {
      // 强制刷新获取菜单逻辑
      if (store.getters.menuList.length <= 0) {
        await getMenus()
        appConfigApi()
          .then(res => {
            if (res.data && res.data.ai_status == "1") {
              AiEmbeddedManage.getAiEmbedded().init(store.getters.token, {
                defaultShow: !isHideAiModule,
              })
            }
          })
          .catch(() => {});
        next(to.fullPath)
      } else {
        // 判断是否需要隐藏悬浮球，如果是则隐藏 AI 悬浮窗插件
        if (isHideAiModule) {
          if (typeof AiEmbeddedManage.getAiEmbedded().hide === 'function') AiEmbeddedManage.getAiEmbedded().hide();
        } else {
          if (typeof AiEmbeddedManage.getAiEmbedded().show === 'function') AiEmbeddedManage.getAiEmbedded().show();
        }
        next()
      }
    }
    NProgress.done()
  } else {

    /* has no token*/
    if (whiteList.indexOf(to.name) !== -1) {

      next()
    } else {
      // 没有token时，清空token以及其他缓存
      await store.dispatch('user/resetToken')
      next(`${roterPre}/login?redirect=${to.fullPath}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
