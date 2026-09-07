import { defHttp } from '@/utils/http/axios';
import { Menu, MenuResp, MenuOption } from './model';
import { ID, IDS, PageQuery } from '@/api/base';

enum Api {
  menuList = '/system/menu/list',
  root = '/system/menu',
  roleMenuTree = '/system/menu/roleMenuTreeselect',
  menuTreeSelect = '/system/menu/treeselect',
  tenantPackageMenuTreeselect = '/system/menu/tenantPackageMenuTreeselect',
}

export function menuList(params?: PageQuery) {
  return defHttp.get<Menu[]>({ url: Api.menuList, params });
}

export function menuInfo(menuId: ID) {
  return defHttp.get<Menu>({ url: Api.root + '/' + menuId });
}

export function menuAdd(data: any) {
  return defHttp.postWithMsg<void>({ url: Api.root, data });
}

export function menuUpdate(data: any) {
  return defHttp.putWithMsg<void>({ url: Api.root, data });
}

export function menuRemove(menuIds: IDS) {
  return defHttp.deleteWithMsg<void>({ url: Api.root + '/' + menuIds });
}

/**
 * 返回对应角色的菜单
 * @param roleId id
 * @returns
 */
export function roleMenuTreeSelect(roleId: ID) {
  return defHttp.get<MenuResp>({ url: Api.roleMenuTree + '/' + roleId });
}

/**
 * 下拉框使用  返回所有的菜单
 * @returns
 */
export function menuTreeSelect() {
  return defHttp.get<MenuOption[]>({ url: Api.menuTreeSelect });
}

/**
 * 租户套餐使用
 * @param packageId packageId
 * @returns
 */
export function tenantPackageMenuTreeSelect(packageId: ID) {
  return defHttp.get<MenuResp>({ url: Api.tenantPackageMenuTreeselect + '/' + packageId });
}
