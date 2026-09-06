import { SysUser } from '../system/user'

/** 登录参数 */
export interface LoginParams {
  username: string
  password: string
  code: string
  uuid: string
}

/** 登录响应 */
export interface LoginResponse {
  token: string
  refreshToken: string
}

/** 用户信息 */
export interface UserInfo {
  /** 用户信息 */
  user: SysUser
  /** 角色数据 */
  roles: string[]
  /** 权限数据 */
  permissions: string[]
  /** 初始密码是否提醒修改 */
  isDefaultModifyPwd?: boolean
  /** 密码是否过期 */
  isPasswordExpired?: boolean
  /** 密码修改类型 */
  pwdChrtype?: string
}

/** 验证码响应 */
export interface CaptchaResponse {
  /** 验证码缓存key */
  uuid: string
  /** 验证码图片Base64 */
  img: string
  /** 验证码开关 */
  captchaEnabled: boolean
}
