import { defHttp } from '@/utils/http/axios';
import { ID, IDS, PageQuery, commonExport } from '@/api/base';
import { Role, DeptResp } from './model';

enum Api {
  root = '/system/role',
  roleList = '/system/role/list',
  roleExport = '/system/role/export',
  roleDataScope = '/system/role/dataScope',
  roleChangeStatus = '/system/role/changeStatus',
  roleOptionSelect = '/system/role/optionselect',
  roleAllocatedList = '/system/role/authUser/allocatedList',
  roleUnallocatedList = '/system/role/authUser/unallocatedList',
  roleAuthCancel = '/system/role/authUser/cancel',
  roleAuthCancelAll = '/system/role/authUser/cancelAll',
  roleAuthSelectAll = '/system/role/authUser/selectAll',
  roleDeptTree = '/system/role/deptTree',
}

export function roleList(params?: PageQuery) {
  return defHttp.get<Role[]>({ url: Api.roleList, params });
}

export function roleExport(data: any) {
  return commonExport(Api.roleExport, data);
}

export function roleInfo(roleId: ID) {
  return defHttp.get<Role>({ url: Api.root + '/' + roleId });
}

export function roleAdd(data: any) {
  return defHttp.postWithMsg<void>({ url: Api.root, data });
}

export function roleUpdate(data: any) {
  return defHttp.putWithMsg<void>({ url: Api.root, data });
}

export function roleChangeStatus(data: any) {
  return defHttp.putWithMsg<void>({ url: Api.roleChangeStatus, data });
}

export function roleRemove(roleIds: IDS) {
  return defHttp.deleteWithMsg<void>({ url: Api.root + '/' + roleIds });
}

/**
 * 更新数据权限
 * @param data
 * @returns
 */
export function roleDataScope(data: any) {
  return defHttp.putWithMsg<void>({ url: Api.roleDataScope, data });
}

export function roleOptionSelect(params?: any) {
  return defHttp.get({ url: Api.roleOptionSelect, params });
}

export function roleAllocatedList(params: any) {
  return defHttp.get({ url: Api.roleAllocatedList, params });
}

/**
 * 未授权的用户
 * @param params
 * @returns
 */
export function roleUnallocatedList(params: any) {
  return defHttp.get({ url: Api.roleUnallocatedList, params });
}

/**
 * 取消授权
 * @param data {userId: 2, roleId: "2"}
 * @returns
 */
export function roleAuthCancel(data: any) {
  return defHttp.putWithMsg<void>({ url: Api.roleAuthCancel, data });
}

/**
 * 批量取消授权
 * @param roleId
 * @param userIds
 * @returns
 */
export function roleAuthCancelAll(roleId: string | number, userIds: string[] | number[]) {
  return defHttp.putWithMsg({
    url: `${Api.roleAuthCancelAll}?roleId=${roleId}&userIds=${userIds.join(',')}`,
  });
}

/**
 * 批量授权用户
 * @param roleId
 * @param userIds
 * @returns
 */
export function roleSelectAll(roleId: string | number, userIds: string[] | number[]) {
  return defHttp.putWithMsg({
    url: `${Api.roleAuthSelectAll}?roleId=${roleId}&userIds=${userIds.join(',')}`,
  });
}

/**
 * 部门树
 * @param roleId 角色id
 * @returns
 */
export function roleDeptTree(roleId: string | number) {
  return defHttp.get<DeptResp>({ url: Api.roleDeptTree + '/' + roleId });
}
