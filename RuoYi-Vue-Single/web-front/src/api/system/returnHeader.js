import request from '@/utils/request'

// 查询退库单-主列表
export function listReturnHeader(query) {
  return request({
    url: '/system/returnHeader/list',
    method: 'get',
    params: query
  })
}

// 查询退库单-主详细
export function getReturnHeader(id) {
  return request({
    url: '/system/returnHeader/' + id,
    method: 'get'
  })
}

// 新增退库单-主
export function addReturnHeader(data) {
  return request({
    url: '/system/returnHeader',
    method: 'post',
    data: data
  })
}

// 修改退库单-主
export function updateReturnHeader(data) {
  return request({
    url: '/system/returnHeader',
    method: 'put',
    data: data
  })
}

// 删除退库单-主
export function delReturnHeader(id) {
  return request({
    url: '/system/returnHeader/' + id,
    method: 'delete'
  })
}
