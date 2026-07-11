import request from '@/utils/request'

// 查询知识库主题权限用户列表
export function listUser(query) {
  return request({
    url: '/topic/user/list',
    method: 'get',
    params: query
  })
}

// 查询知识库主题权限用户详细
export function getUser(id) {
  return request({
    url: '/topic/user/' + id,
    method: 'get'
  })
}

// 新增知识库主题权限用户
export function addUser(data) {
  return request({
    url: '/topic/user',
    method: 'post',
    data: data
  })
}

// 修改知识库主题权限用户
export function updateUser(data) {
  return request({
    url: '/topic/user',
    method: 'put',
    data: data
  })
}

// 删除知识库主题权限用户
export function delUser(id) {
  return request({
    url: '/topic/user/' + id,
    method: 'delete'
  })
}
