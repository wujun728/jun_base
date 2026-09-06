import {
  SysUser,
  UserProfileAvatarResult,
  UserProfileResult
} from '@/types/api/system/user'
import response from '@/utils/http'

// 获取用户信息
// 查询用户个人信息
export function getUserProfile() {
  return response<UserProfileResult>({
    url: '/system/user/profile',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data: SysUser) {
  return response({
    url: '/system/user/profile',
    method: 'put',
    data: data
  })
}

// 用户密码重置
export function updateUserPwd(oldPassword: string, newPassword: string) {
  return response({
    url: '/system/user/profile/updatePwd',
    method: 'put',
    data: {
      oldPassword,
      newPassword
    }
  })
}

// 用户头像上传
export function uploadAvatar(file: FormData | File) {
  return response<UserProfileAvatarResult>({
    url: '/system/user/profile/avatar',
    method: 'post',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    data: file
  })
}
