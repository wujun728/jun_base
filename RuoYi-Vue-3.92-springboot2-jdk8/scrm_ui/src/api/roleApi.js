import request from '@/utils/request';

export function menuListApi() {
  return request({
    url: '/getRouters',
    method: 'GET',
  });
}