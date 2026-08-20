import { constantRoutes, dynamicRoutes } from "@/router";
import { store } from "@/stores";
import router from "@/router";
import { useUserStoreHook } from "@/stores/user";
import { isExternal } from "@/utils";
import auth from "@/plugins/auth";

import { getRouters } from "@/api/menu";
const modules = import.meta.glob("../views/**/*.vue");
const Layout = () => import("../layouts/index.vue");

/**
 * 解析组件路径
 *
 * 支持 xxx.vue 与 xxx/index.vue 两种写法，未命中时回退到 404
 */
function resolveComponent(componentPath) {
  const normalized = componentPath
    .trim()
    .replace(/^\/+/, "")
    .replace(/\.vue$/i, "");
  return (
    modules[`../views/${normalized}.vue`] ||
    modules[`../views/${normalized}/index.vue`] ||
    modules[`../views/error/404.vue`]
  );
}

export const usePermissionStore = defineStore("permission", () => {
  const routes = ref([]);
  const mixLayoutSideMenus = ref([]);
  const isRouteGenerated = ref(false);

  /** 生成动态路由 */
  async function generateRoutes() {
    try {
      const res = await getRouters();
      const routeData = res?.data || [];
      const menuRoutes = transformRoutes(routeData);
      const registerRoutes = filterRoutes(menuRoutes);

      routes.value = [...constantRoutes, ...menuRoutes];
      isRouteGenerated.value = true;

      // 追加有权限的隐藏动态路由（分配角色/字典数据/修改生成配置等）
      const asyncRoutes = filterDynamicRoutes(dynamicRoutes);
      asyncRoutes.forEach((route) => router.addRoute(route));

      return registerRoutes;
    } catch (error) {
      isRouteGenerated.value = false;
      throw error;
    }
  }

  /** 设置混合布局左侧菜单 */
  const setMixLayoutSideMenus = (parentPath) => {
    const parentMenu = routes.value.find((item) => item.path === parentPath);
    mixLayoutSideMenus.value = parentMenu?.children || [];
  };

  /** 重置路由状态 */
  const resetRouter = () => {
    const constantNames = new Set(constantRoutes.map((route) => route.name).filter(Boolean));
    routes.value.forEach((route) => {
      if (route.name && !constantNames.has(route.name)) {
        router.removeRoute(route.name);
      }
    });

    routes.value = [...constantRoutes];
    mixLayoutSideMenus.value = [];
    isRouteGenerated.value = false;
  };

  let pendingReload = null;

  /**
   * 重新加载动态路由
   *
   * 同一时刻只允许一个请求进行中
   */
  async function reloadRoutes() {
    if (pendingReload) return pendingReload;

    pendingReload = (async () => {
      try {
        resetRouter();
        const dynamicRoutes = await generateRoutes();
        dynamicRoutes.forEach((route) => {
          router.addRoute(route);
        });
        return dynamicRoutes;
      } finally {
        pendingReload = null;
      }
    })();

    return pendingReload;
  }

  let pendingPermissionRefresh = null;

  /**
   * 刷新权限
   *
   * 重新拉取用户信息后重建动态路由
   */
  async function refreshPermissions() {
    if (pendingPermissionRefresh) return pendingPermissionRefresh;

    pendingPermissionRefresh = (async () => {
      try {
        const userStore = useUserStoreHook();
        await userStore.getUserInfo();
        await reloadRoutes();
      } finally {
        pendingPermissionRefresh = null;
      }
    })();

    return pendingPermissionRefresh;
  }

  return {
    routes,
    mixLayoutSideMenus,
    isRouteGenerated,
    generateRoutes,
    setMixLayoutSideMenus,
    resetRouter,
    reloadRoutes,
    refreshPermissions,
  };
});

/**
 * 将后端路由数据转为 Vue Router 配置
 *
 * 若依后端返回的菜单字段与壳层约定存在差异，此处做归一化：
 * - 顶层 hidden / alwaysShow → meta.hidden / meta.alwaysShow（侧边栏读取 meta）
 * - component 为 Layout 时仅顶层映射为壳层布局，嵌套目录作为分组
 * - ParentView / InnerLink 占位组件按分组处理（标准种子数据不含，防止 404）
 */
const transformRoutes = (routes, isTopLevel = true) => {
  return routes.map((route) => {
    const { component, children, hidden, alwaysShow, meta, ...args } = route;

    const normalizedMeta = {
      ...(meta || {}),
      ...(hidden !== undefined ? { hidden } : {}),
      ...(alwaysShow !== undefined ? { alwaysShow } : {}),
    };

    const normalizedRoute = {
      ...args,
      meta: normalizedMeta,
    };

    if (component === "Layout") {
      normalizedRoute.component = isTopLevel ? Layout : undefined;
    } else if (component === "ParentView" || component === "InnerLink") {
      normalizedRoute.component = undefined;
    } else if (component) {
      normalizedRoute.component = resolveComponent(component);
    }

    if (children && children.length > 0) {
      normalizedRoute.children = transformRoutes(children, false);
    }

    return normalizedRoute;
  });
};

/**
 * 过滤掉不注册为 Vue Router 路由的外链
 */
function filterRoutes(routes) {
  return routes.reduce((result, route) => {
    if (isExternal(route.path)) return result;

    const filtered = { ...route };
    const children = route.children ? filterRoutes(route.children) : [];

    if (children.length > 0) {
      filtered.children = children;
    } else {
      delete filtered.children;
    }

    result.push(filtered);
    return result;
  }, []);
}

/** 非组件环境使用权限store */
export function usePermissionStoreHook() {
  return usePermissionStore(store);
}

/**
 * 根据用户权限过滤隐藏动态路由
 */
function filterDynamicRoutes(routes) {
  const res = [];
  routes.forEach((route) => {
    if (route.permissions) {
      if (auth.hasPermiOr(route.permissions)) {
        res.push(route);
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        res.push(route);
      }
    }
  });
  return res;
}
