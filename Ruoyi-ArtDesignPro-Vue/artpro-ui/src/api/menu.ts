import request from '@/utils/http'
import type { AppRouteRecord } from '@/types/router'
import { normalizeLocalIcon } from '@/utils/ui/iconify-loader'

interface RuoYiRoute {
  name?: string
  path: string
  hidden?: boolean
  redirect?: string
  component?: string
  alwaysShow?: boolean
  meta?: {
    title: string
    icon?: string
    noCache?: boolean
    link?: string
    isIframe?: boolean
    authList?: Array<{ title: string; authMark: string }>
    showBadge?: boolean
    showTextBadge?: string
    isHideTab?: boolean
    fixedTab?: boolean
    activePath?: string
    isFullPage?: boolean
    newTab?: boolean
  }
  children?: RuoYiRoute[]
}

const iconMap: Record<string, string> = {
  system: 'ri:settings-3-line',
  monitor: 'ri:computer-line',
  tool: 'ri:tools-line',
  user: 'ri:user-line',
  peoples: 'ri:team-line',
  tree: 'ri:node-tree',
  post: 'ri:briefcase-line',
  dict: 'ri:book-2-line',
  edit: 'ri:edit-line',
  message: 'ri:notification-3-line',
  log: 'ri:file-list-3-line',
  online: 'ri:global-line',
  job: 'ri:timer-line',
  server: 'ri:server-line',
  redis: 'ri:database-2-line',
  code: 'ri:code-s-slash-line',
  swagger: 'ri:file-code-line',
  dashboard: 'ri:pie-chart-line',
  console: 'ri:home-smile-2-line',
  analysis: 'ri:align-item-bottom-line',
  ecommerce: 'ri:bar-chart-box-line'
}

function normalizeComponent(component?: string): string | undefined {
  if (!component) return undefined
  if (component === 'Layout') return '/index/index'
  if (component === 'ParentView') return undefined
  if (component === 'InnerLink') return '/outside/Iframe'
  return `/${component.replace(/^\/+/, '').replace(/\.vue$/, '')}`
}

function transformRoute(route: RuoYiRoute): AppRouteRecord {
  const link = route.meta?.link
  const component = normalizeComponent(route.component)
  return {
    name: route.name || route.path,
    path: route.path,
    redirect: route.redirect === 'noRedirect' ? undefined : route.redirect,
    component,
    meta: {
      title: route.meta?.title || route.name || route.path,
      icon: normalizeLocalIcon(iconMap[route.meta?.icon || ''] || route.meta?.icon),
      keepAlive: route.meta?.noCache === false,
      isHide: Boolean(route.hidden),
      link,
      isIframe: route.meta?.isIframe === true || route.component === 'InnerLink',
      authList: route.meta?.authList || [],
      showBadge: route.meta?.showBadge,
      showTextBadge: route.meta?.showTextBadge,
      isHideTab: route.meta?.isHideTab,
      fixedTab: route.meta?.fixedTab,
      activePath: route.meta?.activePath,
      isFullPage: route.meta?.isFullPage,
      newTab: route.meta?.newTab
    },
    children: route.children?.map(transformRoute)
  }
}

export function fetchGetMenuList() {
  return request.get<RuoYiRoute[]>({ url: '/getRouters' }).then((routes) => [
    {
      name: 'UserCenter',
      path: '/system/user-center',
      component: '/system/user-center',
      meta: { title: '个人中心', isHide: true }
    },
    ...routes.map(transformRoute)
  ])
}
