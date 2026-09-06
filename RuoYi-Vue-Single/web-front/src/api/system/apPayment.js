import request from '@/utils/request'

// 查询采购付款登记列表
export function listApPayment(query) {
  return request({
    url: '/system/apPayment/list',
    method: 'get',
    params: query
  })
}

// 查询采购付款登记详细
export function getApPayment(id) {
  return request({
    url: '/system/apPayment/' + id,
    method: 'get'
  })
}

// 新增采购付款登记
export function addApPayment(data) {
  return request({
    url: '/system/apPayment',
    method: 'post',
    data: data
  })
}

// 修改采购付款登记
export function updateApPayment(data) {
  return request({
    url: '/system/apPayment',
    method: 'put',
    data: data
  })
}

// 删除采购付款登记
export function delApPayment(id) {
  return request({
    url: '/system/apPayment/' + id,
    method: 'delete'
  })
}
