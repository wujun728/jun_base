import { defHttp } from '@/utils/http/axios';
import { ID, PageQuery } from '@/api/base';
import { Dept } from './model';

enum Api {
  deptList = '/system/dept/list',
  deptNodeInfo = '/system/dept/list/exclude',
  root = '/system/dept',
}

export function deptList(params?: PageQuery) {
  return defHttp.get<Dept[]>({ url: Api.deptList, params });
}

/**
 * 查询部门列表（排除节点）
 * @param deptId 部门ID
 * @returns
 */
export function deptNodeList(deptId: ID) {
  return defHttp.get<Dept[]>({ url: Api.deptNodeInfo + '/' + deptId });
}

export function deptInfo(deptId: ID) {
  return defHttp.get<Dept>({ url: Api.root + '/' + deptId });
}

export function deptAdd(data: any) {
  return defHttp.postWithMsg<void>({ url: Api.root, data });
}

export function deptUpdate(data: any) {
  return defHttp.putWithMsg<void>({ url: Api.root, data });
}

/**
 * 注意这里只允许单删除
 * @param deptId ID
 * @returns
 */
export function deptRemove(deptId: ID) {
  return defHttp.deleteWithMsg<void>({ url: Api.root + '/' + deptId });
}
