import Vue from 'vue';
import Router from 'vue-router';
import { roterPre } from '@/settings';
Vue.use(Router);
/* Layout */
import Layout from '@/layout';
import { generateUniqueString } from '@/libs/helper';

// 获取views中以.vue结尾的文件
const contextInfo = require.context('../views', true, /.vue$/);

/**
 * 过滤/components中的vue文件
 * @returns {{}}
 */
const filteredFileNames = () => {
  const files = {};
  contextInfo.keys().forEach((fileName) => {
    if (!fileName.includes('/components')) {
      const pathConfig = contextInfo(fileName);
      const path = '/' + fileName.substring(2, fileName.length - 4);
      files[path] = pathConfig.default;
    }
  });
  return files;
};
const files = filteredFileNames();

/**
 * 删除不需要配置路由
 * @param items
 * @param paths
 */
const removeItems = (items, paths) => {
  for (let i = 0; i < items.length; i++) {
    const item = items[i];
    for (let j = 0; j < paths.length; j++) {
      const path = paths[j];
      if (item.menu_path === path) {
        items.splice(i, 1);
        i--;
        break;
      } else if (item.children) {
        removeItems(item.children, paths);
        if (item.children.length === 0) {
          delete item.children;
        }
      }
    }
  }
};

/**
 * 处理平台菜单
 * @param menus
 * @param parentPath
 */
const replaceMenuPath = (menus, parentPath) => {
  menus.forEach((menu, index) => {
    if (menu.children) {
      replaceMenuPath(menu.children, menu.menu_path);
    }
    if (parentPath !== '/' &&menu.menu_path&& menu.menu_path.indexOf(parentPath) > -1) {
      menu.new_path = menu.menu_path.replace(parentPath + '/', '');
    }
    // menu.new_path = '/' + menu.component
    menu.component = '/' + menu.component;
  });
};

/**
 * 过滤路由所需要的数据
 * @param routes
 * @param firstRoute
 * @returns {*}
 */
const filterAsyncRoutes = (routes, firstRoute) => {
  return routes.map((route) => {
    const routeRecord = createRouteRecord(route, firstRoute);
    if (route.children != null && route.children && route.children.length) {
      routeRecord.children = filterAsyncRoutes(route.children, false);
    }

    return routeRecord;
  });
};

/**
 * 创建一条路由记录
 * @param route
 * @param firstRoute
 * @returns {{path: (*|string), meta: {noCache: boolean, icon, title: *}, name: *}}
 */
const createRouteRecord = (route, firstRoute) => {
  const routeRecord = {
    // path: route.pid === 0 ? route.menu_path : route.new_path ? route.new_path : '/',
    path: firstRoute ? `/${route.menu_path}` : route.menu_path,
    name: route.unique_auth + '_' + generateUniqueString(),
    meta: {
      title: route.menu_name,
      icon: route.icon,
      noCache: true
    }
  };
  if (route.pid === 0) {
    routeRecord.component = Layout;
    // 解决父级路由不能定义name属性
    delete routeRecord.name;
  } else if (route.pid > 0 && route.children && route.children.length > 0) {
    // 解决父级不写component 属性,子集的component也不会显示问题
    routeRecord.component = { render: (e) => e('router-view') };
  } else {
    routeRecord.component = files[route.component];
  }
  return routeRecord;
};

import defaultRoutes from '@/router/routes';
import companyRouter from '@/router/company';
export const constantRoutes = [...defaultRoutes, companyRouter];

// 解决强制刷新后404找不到页面逻辑
const route404 = { path: '*', redirect: roterPre + '/404', hidden: true };
/**
 * 根据后台菜单动态生成路由
 */
export const getRouterMenus = (entMenuList) => {
  let entRouter = [];
  if (entMenuList && entMenuList.length > 0) {
    // 移除不需要处理路由
    removeItems(entMenuList, ['/admin/user/work']);
    // 处理后台返回的路由结构
    replaceMenuPath(entMenuList, '/');

    entRouter = filterAsyncRoutes(entMenuList);
    constantRoutes.push(...entRouter, route404);

    // 路由重置加载
    resetRouter();
  }
};

const createRouter = () =>
  new Router({
    mode: 'history', // require service support
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
  });

const router = createRouter();

export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher; // reset router
}

export default router;
