import request from '@/utils/request'

// 查询客户信息列表
export function listCustomer(query) {
  return request({
    url: '/system/customer/list',
    method: 'get',
    params: query
  })
}

// 查询客户信息详细
export function getCustomer(id) {
  return request({
    url: '/system/customer/' + id,
    method: 'get'
  })
}

// 新增客户信息
export function addCustomer(data) {
  return request({
    url: '/system/customer',
    method: 'post',
    data: data
  })
}

// 修改客户信息
export function updateCustomer(data) {
  return request({
    url: '/system/customer',
    method: 'put',
    data: data
  })
}

// 删除客户信息
export function delCustomer(id) {
  return request({
    url: '/system/customer/' + id,
    method: 'delete'
  })
}

// 鏌ヨCustomer涓嬫媺鍒楄〃
export function listCustomerSelect(query) {
  return request({
    url: '/system/customer/listBySelect',
    method: 'get',
    params: query
  })
}