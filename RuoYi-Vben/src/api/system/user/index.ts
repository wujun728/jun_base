import { defHttp } from '@/utils/http/axios';
import { ID, IDS, commonExport } from '@/api/base';
import { ContentTypeEnum } from '@/enums/httpEnum';
import { UserImportParam, ResetPwdParam, UserInfoResponse, DeptTree } from './model';

enum Api {
  deptTree = '/system/user/deptTree',
  userList = '/system/user/list',
  root = '/system/user',
  userStatusChange = '/system/user/changeStatus',
  userExport = '/system/user/export',
  userImport = '/system/user/importData',
  userImportTemplate = '/system/user/importTemplate',
  userResetPassword = '/system/user/resetPwd',
  userAuthRole = '/system/user/authRole',
  listDeptUsers = '/system/user/list/dept',
}

export interface DeptTreeData {
  id: number;
  label: string;
  children?: DeptTreeData[];
}

export function userList(params: any) {
  return defHttp.get({ url: Api.userList, params });
}

export function userExport(data: any) {
  return commonExport(Api.userExport, data);
}

/**
 * 从excel导入用户
 * @param data
 * @returns
 */
export function userImportData(data: UserImportParam) {
  return defHttp.post(
    {
      url: Api.userImport,
      data,
      headers: {
        'Content-Type': ContentTypeEnum.FORM_DATA,
      },
    },
    {
      // 返回的msg包含<br> 用modal显示
      successMessageMode: 'modal',
      errorMessageMode: 'modal',
    },
  );
}

/**
 * 下载用户导入模板
 * @returns
 */
export function downloadImportTemplate() {
  return defHttp.post<Blob>(
    { url: Api.userImportTemplate, responseType: 'blob' },
    { isTransformResponse: false },
  );
}

// 可以不传ID  返回部门和角色options 需要获得原始数据
export function userInfo(userId?: ID) {
  const url = userId ? Api.root + '/' + userId : Api.root + '/';
  return defHttp.get<UserInfoResponse>({ url });
}

export function userAdd(data: any) {
  return defHttp.postWithMsg<void>({ url: Api.root, data });
}

export function userUpdate(data: any) {
  return defHttp.putWithMsg<void>({ url: Api.root, data });
}

export function userStatusChange(data: any) {
  return defHttp.putWithMsg<void>({ url: Api.userStatusChange, data });
}

export function userRemove(userIds: IDS) {
  return defHttp.deleteWithMsg({ url: Api.root + '/' + userIds });
}

/**
 * 重置用户密码
 * @param data
 * @returns
 */
export function userResetPassword(data: ResetPwdParam) {
  return defHttp.putWithMsg<void>({ url: Api.userResetPassword, data });
}

/**
 * 这个方法未调用过
 * @param userId
 * @returns
 */
export function getUserAuthRole(userId: ID) {
  return defHttp.get({ url: Api.userAuthRole + '/' + userId });
}

/**
 * 这个方法未调用过
 * @param userId
 * @returns
 */
export function userAuthRoleUpdate(userId: ID, roleIds: number[]) {
  return defHttp.putWithMsg({ url: Api.userAuthRole, data: { userId, roleIds } });
}

/**
 * 获取部门树
 * @returns 部门树数组
 */
export function departmentTree() {
  return defHttp.get<DeptTree[]>({ url: Api.deptTree }, { ignoreCancelToken: true });
}
