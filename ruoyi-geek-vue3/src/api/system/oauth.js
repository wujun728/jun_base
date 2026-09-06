import request from '@/utils/request'

// 查询第三方认证列表
export function listOauth(query) {
  return request({
    url: '/system/oauth/list',
    method: 'get',
    params: query
  })
}

// 查询第三方认证详细
export function getOauth(id) {
  return request({
    url: '/system/oauth/' + id,
    method: 'get'
  })
}

// 新增第三方认证
export function addOauth(data) {
  return request({
    url: '/system/oauth',
    method: 'post',
    data: data
  })
}

// 修改第三方认证
export function updateOauth(data) {
  return request({
    url: '/system/oauth',
    method: 'put',
    data: data
  })
}

// 删除第三方认证
export function delOauth(id) {
  return request({
    url: '/system/oauth/' + id,
    method: 'delete'
  })
}
