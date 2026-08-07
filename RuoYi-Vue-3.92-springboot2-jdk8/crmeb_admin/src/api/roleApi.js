import request from '@/utils/request';

export function getRoleById(pram) {
  return request({
    url: `/system/role/${pram.roles}`,
    method: 'GET',
  });
}

export function menuListApi() {
  return request({
    url: '/getRouters',
    method: 'GET',
  });
}