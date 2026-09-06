// @ts-check

import { createRouter, createWebHistory } from 'vue-router';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import { getToken } from '@/utils/auth.js';
import { useUserStore } from '@/stores/user';
import { usePermissionStore } from '@/stores/permission';
import { notification } from 'ant-design-vue';
import { constantRoutes, notFoundRoute } from './routes';

// 创建路由实例
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: /** @type {import('vue-router').RouteRecordRaw[]} */ (constantRoutes),
  scrollBehavior: () => ({ top: 0 }),
});

// 白名单
const whiteList = ['/login', '/login2', '/login3', '/auth-redirect', '/register', '/404'];
const loginPaths = ['/login', '/login2', '/login3'];
const catchAllPath = '/:pathMatch(.*)*';
let accessRoutesRegistered = false;
const accessRouteDisposers = [];

/**
 * @param {import('vue-router').RouteLocationNormalized} route
 */
const isRouteMatched = (route) => {
  if (route.name && router.hasRoute(route.name)) {
    return true;
  }

  return router.getRoutes().some((registeredRoute) => {
    if (registeredRoute.path === catchAllPath) {
      return false;
    }
    if (registeredRoute.path === route.path) {
      return true;
    }
    return route.path.startsWith(`${registeredRoute.path}/`);
  });
};

/**
 * @param {import('vue-router').RouteRecordRaw[]} routes
 */
const addAccessRoutes = (routes) => {
  let registered = false;

  routes.forEach((route) => {
    const isNamedRouteRegistered = route.name && router.hasRoute(route.name);
    const isPathRegistered = router.getRoutes().some((registeredRoute) => {
      return registeredRoute.path === route.path;
    });

    if (isNamedRouteRegistered || (!route.name && isPathRegistered)) {
      return;
    }

    accessRouteDisposers.push(router.addRoute(route));
    registered = true;
  });

  if (registered) {
    accessRoutesRegistered = true;
  }

  return registered;
};

const addNotFoundRoute = () => {
  if (!router.hasRoute('NotFound')) {
    router.addRoute(notFoundRoute);
  }
};

/**
 * @param {ReturnType<typeof usePermissionStore>} permissionStore
 */
const resetPermissionState = (permissionStore) => {
  while (accessRouteDisposers.length) {
    const removeRoute = accessRouteDisposers.pop();
    if (removeRoute) {
      removeRoute();
    }
  }
  accessRoutesRegistered = false;
  permissionStore.resetRoutes();
};

/**
 * @param {ReturnType<typeof useUserStore>} userStore
 */
const ensureUserReady = async (userStore) => {
  if (!userStore.userLoaded) {
    await userStore.getInfo();
  }
};

/**
 * @param {ReturnType<typeof usePermissionStore>} permissionStore
 */
const ensurePermissionReady = async (permissionStore) => {
  const accessRoutes = await permissionStore.generateRoutes();
  const registered = !accessRoutesRegistered && addAccessRoutes(accessRoutes);
  addNotFoundRoute();

  return registered;
};

/**
 * @param {ReturnType<typeof useUserStore>} userStore
 * @param {ReturnType<typeof usePermissionStore>} permissionStore
 */
const clearUserSession = async (userStore, permissionStore) => {
  try {
    await userStore.logout();
  } catch (logoutError) {
    console.error('清理登录态失败，执行前端兜底登出:', logoutError);
    userStore.fedLogout();
  } finally {
    resetPermissionState(permissionStore);
  }
};

// 保存页面状态
/** @param {import('vue-router').RouteLocationNormalized} from */
const savePageState = (from) => {
  const currentState = {
    scrollPosition: window.scrollY,
  };
  sessionStorage.setItem(`pageState_${from.fullPath}`, JSON.stringify(currentState));
};

// 恢复页面状态
/** @param {import('vue-router').RouteLocationNormalized} to */
const restorePageState = (to) => {
  const savedState = sessionStorage.getItem(`pageState_${to.fullPath}`);
  if (savedState) {
    const state = JSON.parse(savedState);
    window.scrollTo(0, state.scrollPosition);
  }
};

// 路由守卫
router.beforeEach(async (to, from, next) => {
  NProgress.start();
  const hasToken = getToken();
  const userStore = useUserStore();
  const permissionStore = usePermissionStore();

  if (!hasToken) {
    if (permissionStore.routesLoaded || accessRoutesRegistered) {
      resetPermissionState(permissionStore);
    }

    if (whiteList.includes(to.path)) {
      next();
    } else {
      next(`/login?redirect=${encodeURIComponent(to.fullPath)}`);
      NProgress.done();
    }
    return;
  }

  if (loginPaths.includes(to.path)) {
    next({ path: '/home/workbench' });
    NProgress.done();
    return;
  }

  try {
    await ensureUserReady(userStore);
    const routesRegistered = await ensurePermissionReady(permissionStore);

    if (!isRouteMatched(to)) {
      next('/404');
      NProgress.done();
      return;
    }

    if (routesRegistered) {
      next({ ...to, replace: true });
      return;
    }

    next();
  } catch (error) {
    console.error('路由守卫处理失败:', error);
    await clearUserSession(userStore, permissionStore);
    const isHandledError = Boolean(error && typeof error === 'object' && error._handled);
    if (!isHandledError) {
      notification.error({
        message: '错误',
        description: '获取用户信息失败，请重新登录',
      });
    }
    next(`/login?redirect=${encodeURIComponent(to.fullPath)}`);
    NProgress.done();
  }
});

// 错误处理
router.onError((error) => {
  console.error('路由错误:', error);
  const isHandledError = Boolean(error && typeof error === 'object' && error._handled);
  if (isHandledError) {
    return;
  }
  notification.error({
    message: '页面加载失败',
    description: '请检查网络连接或刷新页面重试',
  });
});

// 路由后置守卫
router.afterEach((to, from) => {
  if (from.name) {
    savePageState(from);
  }

  if (to.name) {
    restorePageState(to);
  }

  document.body.classList.add('page-transition');
  setTimeout(() => {
    document.body.classList.remove('page-transition');
  }, 300);

  NProgress.done();
});

export default router;
