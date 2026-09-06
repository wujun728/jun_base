import { defineStore } from 'pinia';
import { constantRoutes } from '@/router/routes';
import { getRouters } from '@/api/system/menu';

// 预加载所有视图组件
const modules = import.meta.glob('../views/**/*.vue');
const moduleLoaders = new Map(
  Object.entries(modules).map(([modulePath, loader]) => {
    const viewPath = modulePath.split('views/')[1].replace(/\.vue$/, '');
    return [viewPath, loader];
  })
);

// 过滤异步路由
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter((route) => {
    // 检查是否是外链路由
    if (isExternalLink(route.path)) {
      // 外链路由不添加到Vue Router中，只在菜单中显示
      return false;
    }

    if (type && route.children) {
      route.children = filterChildren(route.children, lastRouter);
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = () => import('@/layout/BaseLayout.vue');
      } else if (route.component === 'ParentView') {
        route.component = () => import('@/layout/ParentView/index.vue');
      } else if (route.component === 'InnerLink') {
        route.component = () => import('@/layout/InnerLink/index.vue');
      } else {
        route.component = loadView(route.component);
      }
    }
    if (route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, route, type);
    } else {
      delete route['children'];
      delete route['redirect'];
    }
    return true;
  });
}

// 判断是否是外链
function isExternalLink(path) {
  return /^(https?:|mailto:|tel:)/.test(path);
}

// 过滤异步路由（用于侧边栏，保留外链）
function filterAsyncRouterForSidebar(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter((route) => {
    if (type && route.children) {
      route.children = filterChildrenForSidebar(route.children, lastRouter);
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = () => import('@/layout/BaseLayout.vue');
      } else if (route.component === 'ParentView') {
        route.component = () => import('@/layout/ParentView/index.vue');
      } else if (route.component === 'InnerLink') {
        route.component = () => import('@/layout/InnerLink/index.vue');
      } else {
        route.component = loadView(route.component);
      }
    }
    if (route.children && route.children.length) {
      route.children = filterAsyncRouterForSidebar(route.children, route, type);
    }
    return true;
  });
}

function filterChildren(childrenMap, lastRouter = false) {
  var children = [];
  childrenMap.forEach((el, _index) => {
    // 跳过外链路由
    if (isExternalLink(el.path)) {
      return;
    }

    if (el.children && el.children.length) {
      if (el.component === 'ParentView' && !lastRouter) {
        el.children.forEach((c) => {
          // 跳过外链路由
          if (isExternalLink(c.path)) {
            return;
          }
          // 确保路径拼接时去除多余的斜杠
          c.path = `${el.path}/${c.path}`.replace(/\/+/g, '/');
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c));
            return;
          }
          children.push(c);
        });
        return;
      }
    }
    if (lastRouter) {
      // 确保路径拼接时去除多余的斜杠
      el.path = `${lastRouter.path}/${el.path}`.replace(/\/+/g, '/');
      if (el.children && el.children.length) {
        children = children.concat(filterChildren(el.children, el));
        return;
      }
    }
    children = children.concat(el);
  });
  return children;
}

function filterChildrenForSidebar(childrenMap, lastRouter = false) {
  var children = [];
  childrenMap.forEach((el, _index) => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView' && !lastRouter) {
        el.children.forEach((c) => {
          // 对于外链，不进行路径拼接
          if (!isExternalLink(c.path)) {
            // 确保路径拼接时去除多余的斜杠
            c.path = `${el.path}/${c.path}`.replace(/\/+/g, '/');
          }
          if (c.children && c.children.length) {
            children = children.concat(filterChildrenForSidebar(c.children, c));
            return;
          }
          children.push(c);
        });
        return;
      }
    }
    if (lastRouter && !isExternalLink(el.path)) {
      // 确保路径拼接时去除多余的斜杠
      el.path = `${lastRouter.path}/${el.path}`.replace(/\/+/g, '/');
      if (el.children && el.children.length) {
        children = children.concat(filterChildrenForSidebar(el.children, el));
        return;
      }
    }
    children = children.concat(el);
  });
  return children;
}

// 路由懒加载
export const loadView = (view) => {
  const normalizedView = String(view || '')
    .replace(/^\/+/, '')
    .replace(/\.vue$/, '');
  const loader = moduleLoaders.get(normalizedView);

  if (!loader) {
    // console.warn(`找不到组件: ${view}，可用路径:`, Object.keys(modules));
    return () => import('../views/error/404.vue');
  }
  return () => loader();
};

let routesLoadingPromise = null;

const cloneRoutes = (routes) => JSON.parse(JSON.stringify(Array.isArray(routes) ? routes : []));

const collectRouteNames = (routes) => {
  const routeNames = [];
  const collect = (routeList) => {
    routeList.forEach((route) => {
      if (route.name) {
        routeNames.push(route.name);
      }
      if (route.children && route.children.length) {
        collect(route.children);
      }
    });
  };

  collect(routes);
  return routeNames;
};

const buildPermissionRoutes = (remoteRoutes) => {
  const sidebarRoutes = filterAsyncRouterForSidebar(cloneRoutes(remoteRoutes), false, true);
  const rewriteRoutes = filterAsyncRouter(cloneRoutes(remoteRoutes), false, true);
  const defaultRoutes = filterAsyncRouterForSidebar(cloneRoutes(remoteRoutes), false, true);

  return {
    sidebarRoutes,
    rewriteRoutes,
    defaultRoutes,
    routeNames: collectRouteNames(rewriteRoutes),
  };
};

export const usePermissionStore = defineStore('permission', {
  state: () => ({
    routes: [],
    addRoutes: [],
    defaultRoutes: [],
    topbarRouters: [],
    sidebarRouters: [],
    routesLoaded: false,
    menuVersion: 0,
    routeNames: [],
  }),

  actions: {
    setRoutes(routes) {
      this.addRoutes = routes;
      this.routes = constantRoutes.concat(routes);
    },
    setDefaultRoutes(routes) {
      this.defaultRoutes = constantRoutes.concat(routes);
    },
    setTopbarRoutes(routes) {
      this.topbarRouters = routes;
    },
    setSidebarRouters(routes) {
      this.sidebarRouters = routes;
    },
    setRouteNames(routeNames) {
      this.routeNames = routeNames;
    },
    resetRoutes() {
      this.routes = [];
      this.addRoutes = [];
      this.defaultRoutes = [];
      this.topbarRouters = [];
      this.sidebarRouters = [];
      this.routesLoaded = false;
      this.menuVersion += 1;
      this.routeNames = [];
      routesLoadingPromise = null;
    },

    generateRoutes(forceReload = false) {
      if (this.routesLoaded && !forceReload) {
        return Promise.resolve(this.addRoutes);
      }

      if (routesLoadingPromise && !forceReload) {
        return routesLoadingPromise;
      }

      const requestMenuVersion = this.menuVersion;
      routesLoadingPromise = getRouters()
        .then((res) => {
          if (requestMenuVersion !== this.menuVersion) {
            return this.addRoutes;
          }

          const { sidebarRoutes, rewriteRoutes, defaultRoutes, routeNames } = buildPermissionRoutes(
            res.data
          );

          this.setRoutes(rewriteRoutes);
          this.setSidebarRouters(sidebarRoutes);
          this.setDefaultRoutes(sidebarRoutes);
          this.setTopbarRoutes(defaultRoutes);
          this.setRouteNames(routeNames);
          this.routesLoaded = true;

          return rewriteRoutes;
        })
        .finally(() => {
          routesLoadingPromise = null;
        });

      return routesLoadingPromise;
    },
  },
});
