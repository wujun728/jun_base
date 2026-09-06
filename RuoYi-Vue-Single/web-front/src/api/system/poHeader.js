import request from '@/utils/request'

// 查询采购订单-主列表
export function listPoHeader(query) {
  return request({
    url: '/system/poHeader/list',
    method: 'get',
    params: query
  })
}

// 查询采购订单-主详细
export function getPoHeader(id) {
  return request({
    url: '/system/poHeader/' + id,
    method: 'get'
  })
}

// 新增采购订单-主
export function addPoHeader(data) {
  return request({
    url: '/system/poHeader',
    method: 'post',
    data: data
  })
}

// 修改采购订单-主
export function updatePoHeader(data) {
  return request({
    url: '/system/poHeader',
    method: 'put',
    data: data
  })
}

// 删除采购订单-主
export function delPoHeader(id) {
  return request({
    url: '/system/poHeader/' + id,
    method: 'delete'
  })
}
