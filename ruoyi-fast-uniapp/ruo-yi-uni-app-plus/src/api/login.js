import request from '@/utils/request'
import config from '@/config'
// import { LoginData, LoginResult, VerifyCodeResult, TenantInfo } from '@/api/types';
const clientId = config.clientID;
// 登录方法

export function login(data) {
  const params = {
    ...data,
    clientId: data.clientId || clientId,
    grantType: data.grantType || 'password'
  };
  return request({
    url: '/auth/login',
    headers: {
      isToken: false,
      isEncrypt: true
    },
    method: 'POST',
    data: params
  });
}
// export function login(username, password, code, uuid) {
//   const data = {
//     username,
//     password,
//     code,
//     uuid
//   }
//   return request({
//     url: '/login',
//     headers: {
//       isToken: false
//     },
//     method: 'POST',
//     data: data
//   })
// }

// 注册方法
export function register(data) {
  return request({
    url: '/register',
    headers: {
      isToken: false
    },
    method: 'POST',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/system/user/getInfo',
    method: 'GET'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'POST'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/auth/code',
    headers: {
      isToken: false
    },
    method: 'GET',
    timeout: 20000
  })
}

// 获取租户列表
export function getTenantList(){
  return request({
    url: '/auth/tenant/list',
    headers: {
      isToken: false
    },
    method: 'GET'
  });
}