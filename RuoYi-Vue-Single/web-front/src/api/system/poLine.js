import request from '@/utils/request'

// 查询采购订单-明细列表
export function listPoLine(query) {
  return request({
    url: '/system/poLine/list',
    method: 'get',
    params: query
  })
}

// 查询采购订单-明细详细
export function getPoLine(id) {
  return request({
    url: '/system/poLine/' + id,
    method: 'get'
  })
}

// 新增采购订单-明细
export function addPoLine(data) {
  return request({
    url: '/system/poLine',
    method: 'post',
    data: data
  })
}

// 修改采购订单-明细
export function updatePoLine(data) {
  return request({
    url: '/system/poLine',
    method: 'put',
    data: data
  })
}

// 删除采购订单-明细
export function delPoLine(id) {
  return request({
    url: '/system/poLine/' + id,
    method: 'delete'
  })
}
