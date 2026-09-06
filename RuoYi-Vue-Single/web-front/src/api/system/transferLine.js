import request from '@/utils/request'

// 查询调拨单-明细列表
export function listTransferLine(query) {
  return request({
    url: '/system/transferLine/list',
    method: 'get',
    params: query
  })
}

// 查询调拨单-明细详细
export function getTransferLine(id) {
  return request({
    url: '/system/transferLine/' + id,
    method: 'get'
  })
}

// 新增调拨单-明细
export function addTransferLine(data) {
  return request({
    url: '/system/transferLine',
    method: 'post',
    data: data
  })
}

// 修改调拨单-明细
export function updateTransferLine(data) {
  return request({
    url: '/system/transferLine',
    method: 'put',
    data: data
  })
}

// 删除调拨单-明细
export function delTransferLine(id) {
  return request({
    url: '/system/transferLine/' + id,
    method: 'delete'
  })
}
