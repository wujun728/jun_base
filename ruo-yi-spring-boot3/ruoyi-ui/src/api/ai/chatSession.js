import request from '@/utils/request'

// 查询聊天会话列表
export function listSession(query) {
  return request({
    url: '/ai/session/list',
    method: 'get',
    params: query
  })
}

// 查询聊天会话详细
export function getSession(id) {
  return request({
    url: '/ai/session/' + id,
    method: 'get'
  })
}

// 新增聊天会话
export function addSession(data) {
  return request({
    url: '/ai/session',
    method: 'post',
    data: data
  })
}

// 修改聊天会话
export function updateSession(data) {
  return request({
    url: '/ai/session',
    method: 'put',
    data: data
  })
}

// 删除聊天会话
export function delSession(id) {
  return request({
    url: '/ai/session/' + id,
    method: 'delete'
  })
}

// 删除所有聊天会话
export function delAllSession() {
  return request({
    url: '/ai/session/clean',
    method: 'delete'
  })
}
