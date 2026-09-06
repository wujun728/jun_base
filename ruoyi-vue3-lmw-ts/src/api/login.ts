import { request } from '@/utils/hsj/service/index'

// 登录方法
export function login(userInfo: LoginApi) {
  const data = {
    username: userInfo.username.trim(),
    password: userInfo.password.trim(),
    code: userInfo.code,
    uuid: userInfo.uuid,
  }
  return request<LoginRes>({
    url: '/login',
    headers: {
      isToken: false,
    },
    method: 'post',
    data,
  })
}

// 注册方法
export function register(data: anyObj) {
  return request({
    url: '/register',
    headers: {
      isToken: false,
    },
    method: 'post',
    data: data,
  })
}

// 获取用户详细信息
export function getInfo() {
  return request<GetInfoRes>({
    url: '/getInfo',
    method: 'get',
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post',
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false,
    },
    method: 'get',
    timeout: 20000,
  })
}
