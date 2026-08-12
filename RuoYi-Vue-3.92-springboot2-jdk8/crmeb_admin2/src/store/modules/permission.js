import { asyncRoutes, constantRoutes } from '@/router';
import * as roleApi from '@/api/roleApi.js';

const ICON_MAP = {
  'system': 'setting',
  'user': 'user',
  'peoples': 's-custom',
  'tree-table': 'menu',
  'tree': 'share',
  'post': 'office-building',
  'dict': 'collection',
  'edit': 'edit',
  'message': 'message',
  'monitor': 'monitor',
  'online': 'connection',
  'job': 'timer',
  'druid': 'odometer',
  'server': 'cpu',
  'redis': 'coin',
  'redis-list': 'tickets',
  'log': 'document',
  'tool': 's-tools',
  'code': 'document-copy',
  'guide': 'guide',
  'build': 'document-add',
  'swagger': 'connection',
  'form': 'document',
  'logininfor': 'reading',
  'cache': 's-data',
  'dashboard': 's-home',
};

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

      let menus = transformRuoYiMenu(menuData);
      const flatMenus = flattenMenu(menus);

      const accessedRoutes = comRouter(flatMenus, asyncRoutes);
      commit('SET_ROUTES', menus);
      commit('SET_TOPBAR_ROUTES', menus);
      if (this.state.settings && this.state.settings.topNav) {
        commit('SET_SIDEBAR_ROUTERS', state.sidebarRouters.length ? state.sidebarRouters : (menus[0] && menus[0].childList));
      } else {
        commit('SET_SIDEBAR_ROUTERS', menus);
      }
      resolve(accessedRoutes);
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
      icon: ICON_MAP[rawIcon] || rawIcon || 'menu',
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

function flattenMenu(menuList) {
  const result = [];
  function walk(items) {
    items.forEach((item) => {
      result.push(item);
      if (item.children && item.children.length > 0) {
        walk(item.children);
      }
    });
  }
  walk(menuList);
  return result;
}

function comRouter(menus, asyncRouter, hasLeft) {
  const res = [];
  asyncRouter.forEach((router) => {
    const _leftUrl = hasLeft ? hasLeft + '/' + router.path : router.path;
    const _hasPromise = menus.filter((item) => item.url && item.url.startsWith(_leftUrl));
    if (_hasPromise.length > 0) {
      const newRouter = { ...router };
      if (router.children && router.children.length > 0) {
        newRouter.children = comRouter(menus, router.children, _leftUrl);
      }
      res.push(newRouter);
    }
  });
  return res;
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};