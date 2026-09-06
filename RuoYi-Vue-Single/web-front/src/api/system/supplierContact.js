import request from '@/utils/request'

// 查询供货商联系人列表
export function listSupplierContact(query) {
  return request({
    url: '/system/supplierContact/list',
    method: 'get',
    params: query
  })
}

// 查询供货商联系人详细
export function getSupplierContact(id) {
  return request({
    url: '/system/supplierContact/' + id,
    method: 'get'
  })
}

// 新增供货商联系人
export function addSupplierContact(data) {
  return request({
    url: '/system/supplierContact',
    method: 'post',
    data: data
  })
}

// 修改供货商联系人
export function updateSupplierContact(data) {
  return request({
    url: '/system/supplierContact',
    method: 'put',
    data: data
  })
}

// 删除供货商联系人
export function delSupplierContact(id) {
  return request({
    url: '/system/supplierContact/' + id,
    method: 'delete'
  })
}
