import request from '@/utils/request'

// 查询调拨单-主列表
export function listTransferHeader(query) {
  return request({
    url: '/system/transferHeader/list',
    method: 'get',
    params: query
  })
}

// 查询调拨单-主详细
export function getTransferHeader(id) {
  return request({
    url: '/system/transferHeader/' + id,
    method: 'get'
  })
}

// 新增调拨单-主
export function addTransferHeader(data) {
  return request({
    url: '/system/transferHeader',
    method: 'post',
    data: data
  })
}

// 修改调拨单-主
export function updateTransferHeader(data) {
  return request({
    url: '/system/transferHeader',
    method: 'put',
    data: data
  })
}

// 删除调拨单-主
export function delTransferHeader(id) {
  return request({
    url: '/system/transferHeader/' + id,
    method: 'delete'
  })
}
