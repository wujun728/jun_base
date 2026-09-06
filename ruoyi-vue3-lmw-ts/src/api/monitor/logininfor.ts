import { request } from '@/utils/hsj/service/index'

// 查询登录日志列表
export function list(query: anyObj) {
  return request({
    url: '/monitor/logininfor/list',
    method: 'get',
    params: query,
  })
}

// 解锁用户登录状态
export function unlockLogininfor(userName: string) {
  return request({
    url: '/monitor/logininfor/unlock/' + userName,
    method: 'get',
  })
}
