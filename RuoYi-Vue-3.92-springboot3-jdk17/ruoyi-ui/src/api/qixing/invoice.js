import request from '@/utils/request'

// 查询项目开票列表
export function listInvoice(query) {
  return request({
    url: '/system/invoice/list',
    method: 'get',
    params: query
  })
}

// 查询项目开票详细
export function getInvoice(id) {
  return request({
    url: '/system/invoice/' + id,
    method: 'get'
  })
}

// 新增项目开票
export function addInvoice(data) {
  return request({
    url: '/system/invoice',
    method: 'post',
    data: data
  })
}

// 修改项目开票
export function updateInvoice(data) {
  return request({
    url: '/system/invoice',
    method: 'put',
    data: data
  })
}

// 删除项目开票
export function delInvoice(id) {
  return request({
    url: '/system/invoice/' + id,
    method: 'delete'
  })
}

// 鏌ヨInvoice涓嬫媺鍒楄〃
export function listInvoiceSelect(query) {
  return request({
    url: '/system/invoice/listBySelect',
    method: 'get',
    params: query
  })
}