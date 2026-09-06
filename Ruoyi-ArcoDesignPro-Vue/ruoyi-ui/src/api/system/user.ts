import axios from 'axios';
import {parseStrEmpty} from "@/utils/common";
import {DeptRecord} from "@/api/system/detp";

export interface DeptTreeData {
  id: number;
  label: string;
  children?: DeptTreeData[];
}

export interface UserRecord {
  searchValue?: any;
  createBy?: string;
  createTime?: string;
  updateBy?: any;
  updateTime?: any;
  remark?: string;
  userId: number | undefined;
  deptId?: number;
  userName?: string;
  nickName?: string;
  email?: string;
  phonenumber?: string;
  sex?: string;
  avatar?: string;
  password?: any;
  status?: string;
  delFlag?: string;
  loginIp?: string;
  loginDate?: string;
  dept?: DeptRecord;
  roles?: any[];
  roleIds?: any;
  postIds?: any;
  roleId?: any;
  admin?: boolean;
}

export interface UserQueryParams {
  pageNum?: any;
  pageSize?: any;
  userName?: any;
  phonenumber?: any;
  status?: any;
  deptId?: any;
  params?: any;
}

// 查询部门下拉树结构
export function getDeptTreeSelect() {
    return axios({
        url: '/system/user/deptTree',
        method: 'get'
    })
}

// 查询用户列表
export function listUser(query: UserQueryParams) {
    return axios({
        url: '/system/user/list',
        method: 'get',
        params: query
    })
}

// 查询用户详细
export function getUser(userId: any) {
    return axios({
        url: `/system/user/${parseStrEmpty(userId)}`,
        method: 'get'
    })
}

// 新增用户
export function addUser(data: UserRecord) {
    return axios({
        url: '/system/user',
        method: 'post',
        data
    })
}

// 修改用户
export function updateUser(data: UserRecord) {
    return axios({
        url: '/system/user',
        method: 'put',
        data
    })
}

// 删除用户
export function delUser(userId: number | undefined) {
    return axios({
        url: `/system/user/${userId}`,
        method: 'delete'
    })
}

// 用户密码重置
export function resetUserPwd(userId: number, password: string) {
  const data = {userId, password};
    return axios({
        url: '/system/user/resetPwd',
        method: 'put',
        data
    })
}

// 用户状态修改
export function changeUserStatus(userId: number | string | undefined, status: number | string) {
  const data = {userId, status};
    return axios({
        url: '/system/user/changeStatus',
        method: 'put',
        data
    })
}

// 查询用户个人信息
export function getUserProfile() {
    return axios({
        url: '/system/user/profile',
        method: 'get'
    })
}

// 修改用户个人信息
export function updateUserProfile(data: any) {
    return axios({
        url: '/system/user/profile',
        method: 'put',
        data
    })
}

// 用户密码重置
export function updateUserPwd(oldPassword: string, newPassword: string) {
  const data = {oldPassword, newPassword};
    return axios({
        url: '/system/user/profile/updatePwd',
        method: 'put',
        params: data
    })
}

// 用户头像上传
export function uploadAvatar(data: any) {
    return axios({
        url: '/system/user/profile/avatar',
        method: 'post',
        data
    })
}

// 查询授权角色
export function getAuthRole(userId: number | string) {
    return axios({
        url: `/system/user/authRole/${userId}`,
        method: 'get'
    })
}

// 保存授权角色
export function updateAuthRole(data: any) {
    return axios({
        url: '/system/user/authRole',
        method: 'put',
        params: data
    })
}
