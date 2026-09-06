import { SysUser, UserFormDataResult, UserQueryParams } from '@/types/api'
import {
  AjaxResult,
  TableDataInfo,
  TreeSelect
} from '@/types/api/system/common'
import { SysUserRoles } from '@/types/api/system/role'
import { UserAuthRoleResult } from '@/types/api/system/user'
import response from '@/utils/http'
import { parseStrEmpty } from '@/utils/sys/ruoyi'

// 查询用户列表
export function listUser(query: UserQueryParams) {
  return response<TableDataInfo<SysUser>>({
    url: '/system/user/list',
    method: 'get',
    params: query
  })
}

// 查询用户详细
export function getUser(userId?: number) {
  return response<UserFormDataResult>({
    url: '/system/user/' + parseStrEmpty(userId),
    method: 'get'
  })
}

// 新增用户
export function addUser(data: SysUser) {
  return response({
    url: '/system/user',
    method: 'post',
    data: data
  })
}

// 修改用户
export function updateUser(data: SysUser) {
  return response({
    url: '/system/user',
    method: 'put',
    data: data
  })
}

// 删除用户
export function delUser(userId: number | number[]) {
  return response({
    url: '/system/user/' + userId,
    method: 'delete'
  })
}

// 用户密码重置
export function resetUserPwd(userId: number, password: string) {
  return response({
    url: '/system/user/resetPwd',
    method: 'put',
    data: {
      userId,
      password
    }
  })
}

// 用户状态修改
export function changeUserStatus(userId: number, status: string) {
  return response({
    url: '/system/user/changeStatus',
    method: 'put',
    data: {
      userId,
      status
    }
  })
}

// // 查询用户个人信息
// export function getUserProfile(): Promise<UserProfileResult> {
//   return response({
//     url: '/system/user/profile',
//     method: 'get'
//   })
// }

// 修改用户个人信息
export function updateUserProfile(data: SysUser): Promise<AjaxResult> {
  return response({
    url: '/system/user/profile',
    method: 'put',
    data: data
  })
}

// 用户密码重置
export function updateUserPwd(
  oldPassword: string,
  newPassword: string
): Promise<AjaxResult> {
  const data = {
    oldPassword,
    newPassword
  }
  return response({
    url: '/system/user/profile/updatePwd',
    method: 'put',
    data: data
  })
}

// // 用户头像上传
// export function uploadAvatar(
//   file: FormData | File
// ): Promise<UserProfileAvatarResult> {
//   return response({
//     url: '/system/user/profile/avatar',
//     method: 'post',
//     headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
//     data: file
//   })
// }

// 查询授权角色
export function getAuthRole(userId: number) {
  return response<UserAuthRoleResult>({
    url: '/system/user/authRole/' + userId,
    method: 'get'
  })
}

// 保存授权角色
export function updateAuthRole(data: SysUserRoles) {
  return response({
    url: '/system/user/authRole',
    method: 'put',
    params: data
  })
}

// 查询部门下拉树结构
export function deptTreeSelect() {
  return response<TreeSelect[]>({
    url: '/system/user/deptTree',
    method: 'get'
  })
}

// 根据参数键名查询参数值
export function getConfigKey(configKey: string) {
  return response<string>({
    url: '/system/config/configKey/' + configKey,
    method: 'get'
  })
}
