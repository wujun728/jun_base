import store from '@/store';
import vm from '@/main';
import { userMenusApi } from '@/api/public';
import { getRouterMenus } from '@/router';
import { roterPre } from '@/settings';

/**
 * 将若依 /getRouters 返回的 RouterVo 转换为 OA 菜单结构
 * 若依: { name, path, component, hidden, meta:{ title, icon }, children }
 * OA  : { id, pid, menu_name, menu_path, icon, component, is_show, unique_auth, children }
 */
const ICON_MAP = {
  system: 'el-icon-setting', user: 'el-icon-user', 'tree-table': 'el-icon-s-grid',
  tree: 'el-icon-share', post: 'el-icon-postcard', peoples: 'el-icon-user-solid',
  dict: 'el-icon-collection', edit: 'el-icon-edit', message: 'el-icon-message',
  log: 'el-icon-document', monitor: 'el-icon-monitor', online: 'el-icon-link',
  job: 'el-icon-timer', druid: 'el-icon-odometer', server: 'el-icon-cpu',
  redis: 'el-icon-coin', 'redis-list': 'el-icon-tickets', form: 'el-icon-edit-outline',
  logininfor: 'el-icon-reading', tool: 'el-icon-setting', code: 'el-icon-cpu',
  build: 'el-icon-document-add', swagger: 'el-icon-connection', guide: 'el-icon-guide'
};

function transformRuoYiMenu(menuList, parentId = 0, parentPath = '') {
  if (!menuList || !Array.isArray(menuList)) return [];
  return menuList.map((item, index) => {
    const id = (item.meta && item.meta.menuId) || item.id || index + 1;
    const rawPath = item.path || '';
    // 若依 path: 顶级为 /system，子级为相对路径 user；父级拼接 + roterPre 前缀保证能命中 OA 路由
    let fullPath;
    if (parentPath) {
      fullPath = rawPath.startsWith('/') ? roterPre + rawPath : parentPath + '/' + rawPath;
    } else {
      fullPath = rawPath.startsWith('/') ? roterPre + rawPath : roterPre + '/' + rawPath;
    }
    const rawIcon = (item.meta && item.meta.icon) || '';
    // 若依 component（如 system/user/index）需转成 OA require.context 的 key（views/system/user/index）
    const rawComponent = item.component || '';
    const component = rawComponent && rawComponent.indexOf('views/') !== 0 && !rawComponent.startsWith('/')
      ? 'views/' + rawComponent
      : rawComponent;
    const transformed = {
      id,
      pid: parentId,
      menu_name: (item.meta && item.meta.title) || item.name || '',
      menu_path: fullPath,
      icon: ICON_MAP[rawIcon] || rawIcon || 'el-icon-menu',
      component,
      is_show: item.hidden ? 0 : 1,
      game_type: 'M',
      unique_auth: item.name || (item.meta && item.meta.title) || '',
      children: []
    };
    if (item.children && item.children.length > 0) {
      transformed.children = transformRuoYiMenu(item.children, id, fullPath);
    }
    return transformed;
  });
}

export function setToken(token) {
  return store.commit('SET_TOKEN', token);
}
export function getToken() {
  return store.getters.token;
}
export function removeToken() {
  vm && vm.closeNotice();
  return store.commit('SET_TOKEN', '');
}

// 获取菜单权限（若依 /getRouters）
export const getMenus = () => {
  return new Promise((resolve, reject) => {
    userMenusApi()
      .then((response) => {
        const data = response.data || [];
        const menus = transformRuoYiMenu(Array.isArray(data) ? data : []);
        store.commit('user/SET_MENU_LIST', menus);
        store.commit('user/SET_MENU_AUTHOR', menus);
        store.dispatch('user/getMember').catch(() => {});
        store.dispatch('user/getDepartment').catch(() => {});
        getRouterMenus(menus);
        resolve();
      })
      .catch((error) => {
        console.log(error);
        reject();
      });
  });
};
