import type { RuoYiAppRouteRecord } from '@/types/router'

import { monitorRoutes } from './monitor'
import { systemRoutes } from './system'
import { toolRoutes } from './tool'

/**
 * 导出所有模块化路由
 */
export const routeModules: RuoYiAppRouteRecord[] = [
  // {
  //   name: 'Index',
  //   path: '/index',
  //   component: 'dashboard',
  //   meta: {
  //     title: '首页',
  //     icon: 'dashboard-3-fill'
  //   }
  // },
  ...systemRoutes,
  ...toolRoutes,
  ...monitorRoutes
]
