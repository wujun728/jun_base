import type {
  LoginParams,
  LoginResponse,
  CaptchaResponse,
  UserInfo
} from '@/types/api'

import request from '@/utils/http'

/**
 * 获取验证码
 * @returns 验证码图片
 */
export function fetchGetCaptcha() {
  return request<CaptchaResponse>({
    url: '/captchaImage',
    method: 'get'
  })
}

/**
 * 登录
 * @param params 登录参数
 * @returns 登录响应
 */
export function fetchLogin(params: LoginParams) {
  return request<LoginResponse>({
    url: '/login',
    data: params,
    method: 'post'
    // showSuccessMessage: true // 显示成功消息
    // showErrorMessage: false // 不显示错误消息
  })
}

/**
 * 获取用户信息
 * @returns 用户信息
 */
export function fetchGetUserInfo() {
  return request<UserInfo>({
    url: '/getInfo',
    method: 'get'
    // 自定义请求头
    // headers: {
    //   'X-Custom-Header': 'your-custom-value'
    // }
  })
}
