import {DEFAULT_LAYOUT} from '../base';
import {AppRouteRecordRaw} from '../types';

const SYSTEM: AppRouteRecordRaw = {
  path: '/system',
  name: 'System',
  component: DEFAULT_LAYOUT,
  meta: {
      locale: 'menu.system',
      title: '系统管理',
      requiresAuth: true,
      icon: 'IconSettings',
      order: 1,
  },
  children: [
      {
          path: 'user',
          name: 'User',
          component: () => import('@/views/system/user/index.vue'),
          meta: {
              locale: 'menu.system.user',
              title: '用户管理',
              requiresAuth: true,
              roles: ['*'],
              icon: 'IconUser',
          },
      },
      {
          path: 'role',
          name: 'Role',
          component: () => import('@/views/system/role/index.vue'),
          meta: {
              locale: 'menu.system.role',
              title: '角色管理',
              requiresAuth: true,
              roles: ['*'],
              icon: 'IconUserGroup',
          },
      },
      {
          path: 'menu',
          name: 'Menu',
          component: () => import('@/views/system/menu/index.vue'),
          meta: {
              locale: 'menu.system.menu',
              title: '菜单管理',
              requiresAuth: true,
              roles: ['*'],
              icon: 'IconMenu',
          },
      },
      {
          path: 'dept',
          name: 'Dept',
          component: () => import('@/views/system/dept/index.vue'),
          meta: {
              locale: 'menu.system.dept',
              title: '部门管理',
              requiresAuth: true,
              roles: ['*'],
              icon: 'IconMindMapping',
          },
      },
      {
          path: 'post',
          name: 'Post',
          component: () => import('@/views/system/post/index.vue'),
          meta: {
              locale: 'menu.system.post',
              title: '岗位管理',
              requiresAuth: true,
              roles: ['*'],
              icon: 'IconIdcard',
          },
      },
      {
          path: 'dict',
          name: 'Dict',
          component: () => import('@/views/system/dict/index.vue'),
          meta: {
              locale: 'menu.system.dict',
              title: '字典管理',
              requiresAuth: true,
              roles: ['*'],
              icon: 'IconBook',
          },
      },
      {
          path: 'config',
          name: 'Config',
          component: () => import('@/views/system/config/index.vue'),
          meta: {
              locale: 'menu.system.config',
              title: '参数管理',
              requiresAuth: true,
              roles: ['*'],
              icon: 'IconAlignCenter',
          },
      },
      {
          path: 'notice',
          name: 'Notice',
          component: () => import('@/views/system/notice/index.vue'),
          meta: {
              locale: 'menu.system.notice',
              title: '通知管理',
              requiresAuth: true,
              roles: ['*'],
              icon: 'IconNotification',
          },
      },
      {
          path: '/log',
          name: 'Log',
          component: '',
          meta: {
              locale: 'menu.system.log',
              title: '日志管理',
              requiresAuth: true,
              icon: 'IconStorage',
              order: 1,
          },
          children: [
              {
                  path: 'operlog',
                  name: 'Operlog',
                  component: () => import('@/views/monitor/operlog/index.vue'),
                  meta: {
                      locale: 'menu.system.operlog',
                      title: '操作日志',
                      requiresAuth: true,
                      roles: ['*'],
                      icon: 'IconSettings',
                  },
              },
              {
                  path: 'logininfor',
                  name: 'Logininfor',
                  component: () => import('@/views/monitor/logininfor/index.vue'),
                  meta: {
                      locale: 'menu.system.logininfor',
                      title: '登陆日志',
                      requiresAuth: true,
                      roles: ['*'],
                      icon: 'IconRobotAdd',
                  },
              },
          ]
      },

  ],
};

export default SYSTEM;
