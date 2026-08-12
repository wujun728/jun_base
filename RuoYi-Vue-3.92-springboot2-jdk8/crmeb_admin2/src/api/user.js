import request from '@/utils/request';

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data: {
      username: data.account,
      password: data.pwd,
      code: data.code,
      uuid: data.uuid,
    },
  });
}

export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get',
  });
}

export function logout() {
  return request({
    url: '/logout',
    method: 'post',
  });
}

export function getCodeImg() {
  return request({
    url: '/captchaImage',
    method: 'get',
  });
}

export function getLoginPicApi() {
  return request({
    url: '/admin/getLoginPic',
    method: 'get',
  });
}