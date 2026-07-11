import request from '@/utils/request'

// 查询外出信息列表
export function listOutsite(query) {
  return request({
    url: '/system/outsite/list',
    method: 'get',
    params: query
  })
}

// 查询外出信息详细
export function getOutsite(id) {
  return request({
    url: '/system/outsite/' + id,
    method: 'get'
  })
}

// 新增外出信息
export function addOutsite(data) {
  return request({
    url: '/system/outsite',
    method: 'post',
    data: data
  })
}

// 修改外出信息
export function updateOutsite(data) {
  return request({
    url: '/system/outsite',
    method: 'put',
    data: data
  })
}

// 删除外出信息
export function delOutsite(id) {
  return request({
    url: '/system/outsite/' + id,
    method: 'delete'
  })
}

// 鏌ヨOutsite涓嬫媺鍒楄〃
export function listOutsiteSelect(query) {
  return request({
    url: '/system/outsite/listBySelect',
    method: 'get',
    params: query
  })
}