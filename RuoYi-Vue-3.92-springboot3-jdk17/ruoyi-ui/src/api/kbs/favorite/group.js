import request from '@/utils/request'

// 查询知识库收藏组列表
export function listGroup(query) {
  return request({
    url: '/favorite/group/list',
    method: 'get',
    params: query
  })
}

// 查询知识库收藏组详细
export function getGroup(id) {
  return request({
    url: '/favorite/group/' + id,
    method: 'get'
  })
}

// 新增知识库收藏组
export function addGroup(data) {
  return request({
    url: '/favorite/group',
    method: 'post',
    data: data
  })
}

// 修改知识库收藏组
export function updateGroup(data) {
  return request({
    url: '/favorite/group',
    method: 'put',
    data: data
  })
}

// 删除知识库收藏组
export function delGroup(id) {
  return request({
    url: '/favorite/group/' + id,
    method: 'delete'
  })
}

// 查询知识库收藏组下拉列表
export function getGroupSelectList() {
  return request({
    url: '/favorite/group/select',
    method: 'get'
  })
}
