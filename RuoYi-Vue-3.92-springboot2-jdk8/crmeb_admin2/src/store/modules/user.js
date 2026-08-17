import { login, logout, getInfo } from '@/api/user';
import { getToken, setToken, removeToken } from '@/utils/auth';
import router, { resetRouter } from '@/router';
import Cookies from 'js-cookie';
import { Loading } from 'element-ui';
import * as roleApi from '@/api/roleApi.js';
import { formatFlatteningRoutes } from '@/utils/system.js';
import { isHttp, isEmpty } from '@/utils/validate.js';
import defAva from '@/assets/imgs/default_avatar.png';

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  introduction: '',
  roles: [],
  isLogin: Cookies.get('isLogin'),
  permissions: [],
  captcha: {
    captchaVerification: '',
    secretKey: '',
    token: '',
  },
  menuList: JSON.parse(localStorage.getItem('MerPlatAdmin_MenuList')) || [],
  oneLvMenus: [],
  oneLvRoutes: JSON.parse(localStorage.getItem('MerPlatAdmin_oneLvRoutes')) || [],
  childMenuList: [],
};

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token;
  },
  SET_ISLOGIN: (state, isLogin) => {
    state.isLogin = isLogin;
    Cookies.set('isLogin', isLogin);
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction;
  },
  SET_NAME: (state, name) => {
    state.name = name;
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar;
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles;
  },
  SET_PERMISSIONS: (state, permissions) => {
    state.permissions = permissions;
  },
  SET_CAPTCHA: (state, captcha) => {
    state.captcha = captcha;
  },
  SET_MENU_LIST: (state, menuList) => {
    state.menuList = menuList;
  },
  setOneLvMenus(state, oneLvMenus) {
    state.oneLvMenus = oneLvMenus;
  },
  setOneLvRoute(state, oneLvRoutes) {
    state.oneLvRoutes = oneLvRoutes;
  },
  childMenuList(state, list) {
    state.childMenuList = list;
  },
};

const actions = {
  login({ commit }, userInfo) {
    Loading.service();
    return new Promise((resolve, reject) => {
      login(userInfo)
        .then((data) => {
          let loadingInstance = Loading.service();
          loadingInstance.close();
          commit('SET_TOKEN', data.token);
          setToken(data.token);
          resolve();
        })
        .catch((error) => {
          reject(error);
        });
    });
  },

  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo()
        .then((res) => {
          if (!res || (!res.user && !(res.data && res.data.user))) {
            reject('Verification failed, please Login again.');
          }
          // 若依返回: { user: { userName, nickName, userId, avatar }, roles, permissions }
          const user = res.user || (res.data && res.data.user) || {};
          const roles = res.roles || (res.data && res.data.roles) || [];
          const permissions = res.permissions || (res.data && res.data.permissions) || [];

          if (!roles || roles.length <= 0) {
            reject('getInfo: roles must be a non-null array!');
          }

          commit('SET_ROLES', roles);
          commit('SET_NAME', user.userName || user.nickName || '');
          let avatar = user.avatar || '';
          if (!isHttp(avatar)) {
            avatar = isEmpty(avatar) ? defAva : process.env.VUE_APP_BASE_API + avatar;
          }
          commit('SET_AVATAR', avatar);
          commit('SET_INTRODUCTION', user.nickName || '');
          commit('SET_PERMISSIONS', permissions);
          resolve({ roles, permissions, user });
        })
        .catch((error) => {
          reject(error);
        });
    });
  },

  handleLogout({ commit, state, dispatch }) {
    Loading.service();
    return new Promise((resolve, reject) => {
      logout()
        .then(() => {
          let loadingInstance = Loading.service();
          loadingInstance.close();
          commit('SET_TOKEN', '');
          commit('SET_ROLES', []);
          commit('SET_PERMISSIONS', []);
          removeToken();
          resetRouter();
          Cookies.remove('storeStaffList');
          Cookies.remove('JavaInfo');
          sessionStorage.removeItem('token');
          dispatch('tagsView/delAllViews', null, { root: true });
          resolve();
        })
        .catch((error) => {
          reject(error);
        });
    });
  },

  resetToken({ commit }) {
    return new Promise((resolve) => {
      commit('SET_TOKEN', '');
      commit('SET_ROLES', []);
      removeToken();
      resolve();
    });
  },

  getMenus({ commit }) {
    return new Promise(async (resolve, reject) => {
      let accessRoutes = await roleApi.menuListApi();
      // 若依返回: { data: [{ path, children, meta: { title, icon } }] }
      const menuData = accessRoutes.data || accessRoutes;
      accessRoutes = transformRuoYiMenu(menuData);
      commit('SET_MENU_LIST', accessRoutes);
      localStorage.setItem('MerPlatAdmin_MenuList', JSON.stringify(accessRoutes));
      let routes = formatFlatteningRoutes(accessRoutes);
      localStorage.setItem('MerPlatAdmin_oneLvRoutes', JSON.stringify(routes));
      commit('setOneLvRoute', routes);
      resolve(resolve);
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

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};