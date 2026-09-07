import type { AppRouteModule } from '@/router/types';
import { LAYOUT } from '@/router/constant';

/**
 * 这里放本地路由  就是后台没有包含的
 * 默认登录后才能访问
 * 白名单路径: router/guard/permissionGuard.ts
 * 注意 component: LAYOUT 一定要有
 */
export const localRoutes: AppRouteModule[] = [
  {
    component: LAYOUT,
    path: '/tool/generate',
    name: 'ToolRoute',
    meta: {
      title: '修改生成配置',
      hidden: true,
    },
    children: [
      {
        path: 'edit/:tableId',
        component: () => import('@/views/tool/gen/EditGenerate.vue'),
        name: 'EditGenerate',
        meta: {
          hidden: true,
          title: '修改生成配置',
          currentActiveMenu: '/tool/gen',
        },
      },
    ],
  },
  {
    component: LAYOUT,
    path: '/account',
    name: 'AccountInfo',
    redirect: '/setting',
    meta: {
      hideMenu: true,
      title: '账号',
    },
    children: [
      {
        path: 'setting',
        name: 'AccountSettingPage',
        component: () => import('@/views/auth/profile/index.vue'),
        meta: {
          title: '个人设置',
        },
      },
    ],
  },
  {
    component: LAYOUT,
    path: '/system/assign-roles',
    name: 'AssignRolesRoot',
    redirect: '/:roleId',
    meta: {
      hideMenu: true,
      title: '分配角色',
    },
    children: [
      {
        path: ':roleId',
        name: 'AssignRoles',
        component: () => import('@/views/system/role/AssignRoles/index.vue'),
        meta: {
          title: '分配角色',
          currentActiveMenu: '/system/role',
        },
      },
    ],
  },
  {
    component: LAYOUT,
    path: '/job-log',
    name: 'JobLogIndex',
    redirect: '/:roleId',
    meta: {
      hideMenu: true,
      title: '',
    },
    children: [
      {
        path: ':jobId',
        name: 'JobLog',
        component: () => import('@/views/monitor/job/log/index.vue'),
        meta: {
          title: '调度日志',
          currentActiveMenu: '/monitor/job',
        },
      },
    ],
  },
];
