import { constantRoutes } from '@/router';
import * as roleApi from '@/api/roleApi.js';
import Layout from '@/layout';
import ParentView from '@/components/ParentView';

const state = {
  routes: [],
  addRoutes: [],
  topbarRouters: [],
  sidebarRouters: [],
};

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes;
    state.routes = routes;
  },
  SET_TOPBAR_ROUTES: (state, routes) => {
    state.topbarRouters = routes;
  },
  SET_SIDEBAR_ROUTERS: (state, routes) => {
    state.sidebarRouters = routes;
  },
};

const actions = {
  generateRoutes({ commit, state }) {
    return new Promise(async (resolve) => {
      let menusAll = await roleApi.menuListApi();
      const menuData = Array.isArray(menusAll) ? menusAll : (menusAll.data || []);

      const menus = transformRuoYiMenu(menuData);
      const sidebarRoutes = filterAsyncRouter(menus);

      commit('SET_ROUTES', sidebarRoutes);
      commit('SET_TOPBAR_ROUTES', menus);
      if (this.state.settings && this.state.settings.topNav) {
        commit('SET_SIDEBAR_ROUTERS', state.sidebarRouters.length ? state.sidebarRouters : (menus[0] && menus[0].childList));
      } else {
        commit('SET_SIDEBAR_ROUTERS', menus);
      }
      resolve(sidebarRoutes);
    });
  },
};

function transformRuoYiMenu(menuList, parentId = 0, parentPath = '') {
  if (!menuList || !Array.isArray(menuList)) return [];
  return menuList.map((item, index) => {
    const id = (item.meta && item.meta.menuId) || item.id || index + 1;
    const rawPath = item.path || '';
    const fullPath = parentPath ? (rawPath.startsWith('/') ? rawPath : parentPath + '/' + rawPath) : rawPath;
    const rawIcon = (item.meta && item.meta.icon) || '';
    const transformed = {
      id,
      pid: parentId,
      name: (item.meta && item.meta.title) || item.name || '',
      title: (item.meta && item.meta.title) || item.name || '',
      url: fullPath || item.path || '',
      component: item.component || '',
      icon: rawIcon || 'menu',
      meta: {
        title: (item.meta && item.meta.title) || item.name || '',
        icon: rawIcon || '',
      },
      sort: (item.meta && item.meta.orderNum) || index,
      type: (item.meta && item.meta.menuType) || 'M',
      hidden: !!item.hidden,
      isShow: !item.hidden,
      menuType: (item.meta && item.meta.menuType) || 'M',
      unique: fullPath,
      path: fullPath,
      isMenu: !(item.meta && item.meta.menuType === 'F'),
      perms: (item.meta && item.meta.perms) || '',
      redirect: '',
      isLink: (item.meta && item.meta.link) || '',
      isIframe: (item.meta && item.meta.isFrame) === '1' || false,
    };
    if (item.children && item.children.length > 0) {
      transformed.childList = transformRuoYiMenu(item.children, id, fullPath);
      transformed.children = transformed.childList;
    } else {
      transformed.childList = [];
      transformed.children = [];
    }
    return transformed;
  });
}

function filterAsyncRouter(asyncRouterMap) {
  return asyncRouterMap.filter(route => {
    if (route.component) {
      if (route.component === 'Layout') {
        route.component = Layout;
      } else if (route.component === 'ParentView') {
        route.component = ParentView;
      } else {
        route.component = loadView(route.component);
      }
    }
    if (route.children && route.children.length > 0) {
      route.children = filterAsyncRouter(route.children);
    } else {
      delete route.children;
      delete route.redirect;
    }
    return true;
  });
}

export const loadView = (view) => {
  if (process.env.NODE_ENV === 'development') {
    return (resolve) => require([`@/views/${view}`], resolve);
  } else {
    return () => import(`@/views/${view}`);
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};