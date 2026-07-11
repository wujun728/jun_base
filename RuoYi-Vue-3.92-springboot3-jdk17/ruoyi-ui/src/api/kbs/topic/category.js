import request from '@/utils/request'

// 查询知识库主题类别列表
export function listCategory(query) {
  return request({
    url: '/topic/category/list',
    method: 'get',
    params: query
  })
}

// 查询知识库主题类别详细
export function getCategory(id) {
  return request({
    url: '/topic/category/' + id,
    method: 'get'
  })
}

// 新增知识库主题类别
export function addCategory(data) {
  return request({
    url: '/topic/category',
    method: 'post',
    data: data
  })
}

// 修改知识库主题类别
export function updateCategory(data) {
  return request({
    url: '/topic/category',
    method: 'put',
    data: data
  })
}

// 删除知识库主题类别
export function delCategory(id) {
  return request({
    url: '/topic/category/' + id,
    method: 'delete'
  })
}

// 查询知识库主题类别下拉列表
export function getCategorySelectList() {
  return request({
    url: '/topic/category/select',
    method: 'get'
  })
}