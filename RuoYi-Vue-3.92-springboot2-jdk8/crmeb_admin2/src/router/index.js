import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

import Layout from '@/layout';
import ruoyiRouter from './modules/ruoyi';

export const constantRoutes = [
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true,
  },
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index'),
      },
    ],
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true,
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true,
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true,
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: '主页', icon: 'dashboard', isAffix: true },
      },
    ],
  },
  ...ruoyiRouter,
  { path: '*', redirect: '/404', hidden: true },
];

export const asyncRoutes = [];

const createRouter = () =>
  new Router({
    mode: 'history',
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes,
  });

const router = createRouter();

export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher;
}

export default router;