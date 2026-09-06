import {REDIRECT_ROUTE_NAME} from '@/router/constants';
import {AppRouteRecordRaw} from './types';

export const DEFAULT_LAYOUT = () => import('@/layout/default-layout.vue');

export const REDIRECT_MAIN: AppRouteRecordRaw = {
  path: '/redirect',
  name: 'redirectWrapper',
  component: DEFAULT_LAYOUT,
  meta: {
    requiresAuth: true,
    hideInMenu: true,
  },
  children: [
    {
      path: '/redirect/:path',
      name: REDIRECT_ROUTE_NAME,
      component: () => import('@/views/redirect/index.vue'),
      meta: {
        requiresAuth: true,
        hideInMenu: true,
      },
    },
  ],
};

export const NOT_FOUND_ROUTE = {
    path: '/:pathMatch(.*)*',
    name: 'notFound',
    component: () => import('@/views/not-found/index.vue'),
};

export const EXCEPTION_403 = {
    path: '/403',
    name: '403',
    component: () => import('@/views/exception/403/index.vue'),
    meta: {
        requiresAuth: true,
        hideInMenu: true,
    },
};

export const EXCEPTION_404 = {
    path: '/404',
    name: '404',
    component: () => import('@/views/exception/404/index.vue'),
    meta: {
        requiresAuth: true,
        hideInMenu: true,
    },
};

export const EXCEPTION_500 = {
    path: '/500',
    name: '500',
    component: () => import('@/views/exception/500/index.vue'),
    meta: {
        requiresAuth: true,
        hideInMenu: true,
    },
};
