import request from '@/utils/request'

// 查询聊天会话详情列表
export function listDetail(query) {
  return request({
    url: '/ai/detail/list',
    method: 'get',
    params: query
  })
}

// 查询聊天会话详情详细
export function getDetail(id) {
  return request({
    url: '/ai/detail/' + id,
    method: 'get'
  })
}

// 新增聊天会话详情
export function addDetail(data) {
  return request({
    url: '/ai/detail',
    method: 'post',
    data: data
  })
}

// 修改聊天会话详情
export function updateDetail(data) {
  return request({
    url: '/ai/detail',
    method: 'put',
    data: data
  })
}

// 删除聊天会话详情
export function delDetail(id) {
  return request({
    url: '/ai/detail/' + id,
    method: 'delete'
  })
}
