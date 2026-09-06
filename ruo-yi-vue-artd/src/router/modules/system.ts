import type { RuoYiAppRouteRecord } from '@/types/router'

export const systemRoutes: RuoYiAppRouteRecord[] = [
  // {
  //   path: '/system/user-auth',
  //   name: 'UserAuth',
  //   component: 'layout/index',
  //   hidden: true,
  //   permissions: ['system:user:edit'],
  //   meta: {},
  //   children: [
  //     {
  //       path: 'role/:userId(\\d+)',
  //       component: 'system/role',
  //       name: 'AuthRole',
  //       meta: { title: '分配角色', activeMenu: '/system/user' }
  //     }
  //   ]
  // },
  {
    path: '/system/role-auth',
    name: 'RoleAuth',
    component: 'layout/index',
    hidden: true,
    permissions: ['system:role:edit'],
    meta: {},
    children: [
      {
        path: 'user/:roleId(\\d+)',
        component: 'system/role',
        name: 'AuthUser',
        meta: { title: '分配用户', activeMenu: '/system/role' }
      }
    ]
  },
  {
    path: '/system/dict-data',
    name: 'DictData',
    component: 'layout/index',
    hidden: true,
    permissions: ['system:dict:list'],
    meta: {},
    children: [
      {
        path: 'index/:dictId(\\d+)',
        component: 'system/dict',
        name: 'Data',
        meta: { title: '字典数据', activeMenu: '/system/dict' }
      }
    ]
  }
]
