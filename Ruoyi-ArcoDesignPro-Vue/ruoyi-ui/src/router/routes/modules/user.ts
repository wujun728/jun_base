import {DEFAULT_LAYOUT} from '../base';
import {AppRouteRecordRaw} from '../types';

const USER: AppRouteRecordRaw = {
  path: '/user',
  name: 'user',
  component: DEFAULT_LAYOUT,
  meta: {
      locale: 'menu.user',
      title: '个人中心',
      icon: 'IconStar',
      requiresAuth: true,
      order: 4,
  },
  children: [
    {
      path: 'info',
      name: 'Info',
      component: () => import('@/views/user/info/index.vue'),
      meta: {
          locale: 'menu.user.info',
          title: '用户信息',
          requiresAuth: true,
          roles: ['*'],
          icon: 'IconUser',
      },
    },
    {
      path: 'setting',
      name: 'Setting',
      component: () => import('@/views/user/setting/index.vue'),
      meta: {
          locale: 'menu.user.setting',
          title: '用户设置',
          requiresAuth: true,
          roles: ['*'],
          icon: 'IconTool',
      },
    },
  ],
};

export default USER;
