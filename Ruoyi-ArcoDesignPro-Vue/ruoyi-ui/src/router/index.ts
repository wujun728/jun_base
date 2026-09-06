import {createRouter, createWebHistory} from 'vue-router';
import NProgress from 'nprogress'; // progress bar
import 'nprogress/nprogress.css';

import {appDynamicRoutes, appRoutes} from './routes';
import {NOT_FOUND_ROUTE, REDIRECT_MAIN, EXCEPTION_403, EXCEPTION_404, EXCEPTION_500} from './routes/base';
import createRouteGuard from './guard';

NProgress.configure({showSpinner: false}); // NProgress Configuration

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: 'login'
    },
      {
          path: '/login',
          name: 'login',
          component: () => import('@/views/login/index.vue'),
          meta: {
              requiresAuth: false,
          },
      },
      ...(appRoutes as any),
      ...(appDynamicRoutes as any),
      // 重定向和未知页面路由
      REDIRECT_MAIN,
      NOT_FOUND_ROUTE,
      // 异常页面的路由
      EXCEPTION_403,
      EXCEPTION_404,
      EXCEPTION_500,
  ],
  scrollBehavior() {
    return {top: 0};
  },
});

createRouteGuard(router);

export default router;
