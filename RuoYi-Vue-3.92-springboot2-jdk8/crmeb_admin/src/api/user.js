import request from '@/utils/request';

export function login(data) {
  return request({
    url: '/login',
    headers: { isToken: false },
    method: 'post',
    data: {
      username: data.username || data.account,
      password: data.pwd || data.password,
      code: data.code,
      uuid: data.uuid
    },
  });
}

export function getInfo(token) {
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
    headers: { isToken: false },
    method: 'get',
    timeout: 20000,
  });
}

export function getLoginPicApi() {
  return new Promise((resolve) => {
    resolve({ banner: [], loginLogo: '', backgroundImage: '', siteName: '若依管理系统' });
  });
}

export function userListApi(params) {
  return request({ url: '/system/user/list', method: 'get', params });
}
export function userUpdateApi(params, data) {
  return request({ url: '/system/user', method: 'put', data });
}
export function userInfoApi(params) {
  return request({ url: '/system/user/' + (params.userId || ''), method: 'get' });
}
export function userDeleteApi(params) {
  return request({ url: '/system/user/' + params.userIds, method: 'delete' });
}
export function levelListApi() {
  return request({ url: '/system/role/list', method: 'get' });
}
export function levelSaveApi(data) {
  return request({ url: '/system/role', method: 'post', data });
}
export function levelUpdateApi(params, data) {
  return request({ url: '/system/role', method: 'put', data });
}
export function levelInfoApi(params) {
  return request({ url: '/system/role/' + params.roleId, method: 'get' });
}
export function levelDeleteApi(id) {
  return request({ url: '/system/role/' + id, method: 'delete' });
}
export function levelUseApi(data) { return Promise.resolve(); }
export function infobyconditionApi(params) { return Promise.resolve(); }
export function topdetailApi(params) { return Promise.resolve(); }
export function groupPiApi(params) { return Promise.resolve(); }
export function tagPiApi(params) { return Promise.resolve(); }
export function foundsApi(params) { return Promise.resolve(); }
export function tagListApi(params) { return Promise.resolve(); }
export function tagSaveApi(data) { return Promise.resolve(); }
export function tagUpdateApi(params, data) { return Promise.resolve(); }
export function tagInfoApi(params) { return Promise.resolve(); }
export function tagDeleteApi(params) { return Promise.resolve(); }
export function groupListApi(params) { return Promise.resolve(); }
export function groupSaveApi(data) { return Promise.resolve(); }
export function groupUpdateApi(params, data) { return Promise.resolve(); }
export function groupInfoApi(params) { return Promise.resolve(); }
export function groupDeleteApi(params) { return Promise.resolve(); }
export function updateSpreadApi(data) { return Promise.resolve(); }
export function updatePhoneApi(params) { return Promise.resolve(); }
export function captchaconfigApi() { return Promise.resolve(); }
export function captchaApi() { return Promise.resolve(); }