import request from '@/utils/http'

export interface CaptchaInfo {
  captchaEnabled: boolean
  captchaType: 'slider' | 'image'
  registerEnabled: boolean
  uuid?: string
  img?: string
}

export function fetchCaptcha() {
  return request.get<CaptchaInfo>({ url: '/captchaImage' })
}

export function fetchLogin(params: Api.Auth.LoginParams) {
  return request.post<Api.Auth.LoginResponse>({ url: '/login', params })
}

export function fetchRegister(params: Api.Auth.RegisterParams) {
  return request.post<void>({ url: '/register', params, showSuccessMessage: true })
}

export function fetchGetUserInfo() {
  return request.get<Api.Auth.RuoYiUserInfo>({ url: '/getInfo' }).then((data) => ({
    ...data.user,
    userName: data.user.userName,
    buttons: data.permissions || [],
    roles: data.roles || []
  }))
}

export function fetchLogout() {
  return request.post<void>({ url: '/logout', showErrorMessage: false })
}
