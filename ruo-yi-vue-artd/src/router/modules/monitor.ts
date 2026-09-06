import type { RuoYiAppRouteRecord } from '@/types/router'

export const monitorRoutes: RuoYiAppRouteRecord[] = [
  {
    path: '/monitor/job-log',
    name: 'JobLog',
    component: 'layout/index',
    hidden: true,
    permissions: ['monitor:job:list'],
    meta: {},
    children: [
      {
        path: 'index/:jobId(\\d+)',
        component: 'monitor/job/log',
        name: 'JobLogIndex',
        meta: { title: '调度日志', activeMenu: '/monitor/job' }
      }
    ]
  }
]
