import { RuoYiAppRouteRecord } from '@/types/router'

export const toolRoutes: RuoYiAppRouteRecord[] = [
  {
    path: '/tool/gen-edit',
    name: 'GenEdit',
    component: 'layout/index',
    hidden: true,
    permissions: ['tool:gen:edit'],
    meta: {},
    children: [
      {
        path: 'index/:tableId(\\d+)',
        component: 'tool/gen/edit',
        name: 'GenEditIndex',
        meta: { title: '修改生成配置', activeMenu: '/tool/gen' }
      }
    ]
  }
]
