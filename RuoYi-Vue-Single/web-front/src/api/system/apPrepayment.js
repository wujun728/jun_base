import request from '@/utils/request'

// 查询采购预付款登记列表
export function listApPrepayment(query) {
  return request({
    url: '/system/apPrepayment/list',
    method: 'get',
    params: query
  })
}

// 查询采购预付款登记详细
export function getApPrepayment(id) {
  return request({
    url: '/system/apPrepayment/' + id,
    method: 'get'
  })
}

// 新增采购预付款登记
export function addApPrepayment(data) {
  return request({
    url: '/system/apPrepayment',
    method: 'post',
    data: data
  })
}

// 修改采购预付款登记
export function updateApPrepayment(data) {
  return request({
    url: '/system/apPrepayment',
    method: 'put',
    data: data
  })
}

// 删除采购预付款登记
export function delApPrepayment(id) {
  return request({
    url: '/system/apPrepayment/' + id,
    method: 'delete'
  })
}
