import {DEFAULT_LAYOUT} from '../base';
import {AppRouteRecordRaw} from '../types';

const DASHBOARD: AppRouteRecordRaw = {
  path: '/dashboard',
  name: 'dashboard',
  component: DEFAULT_LAYOUT,
  meta: {
      locale: 'menu.dashboard',
      title: '仪表盘',
      requiresAuth: true,
      icon: 'IconLayers',
      order: 0,
  },
  children: [
      {
          path: 'workplace',
          name: 'Workplace',
          component: () => import('@/views/dashboard/workplace/index.vue'),
          meta: {
              locale: 'menu.dashboard.workplace',
              title: '工作台',
              requiresAuth: true,
              roles: ['*'],
              icon: 'IconDesktop',
          },
      },
  ],
};

export default DASHBOARD;
