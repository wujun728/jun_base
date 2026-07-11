import request from '@/utils/request'

// 查询工资审核发放列表
export function listPayroll(query) {
  return request({
    url: '/system/payroll/list',
    method: 'get',
    params: query
  })
}

// 查询工资审核发放详细
export function getPayroll(id) {
  return request({
    url: '/system/payroll/' + id,
    method: 'get'
  })
}

// 新增工资审核发放
export function addPayroll(data) {
  return request({
    url: '/system/payroll',
    method: 'post',
    data: data
  })
}

// 修改工资审核发放
export function updatePayroll(data) {
  return request({
    url: '/system/payroll',
    method: 'put',
    data: data
  })
}

// 删除工资审核发放
export function delPayroll(id) {
  return request({
    url: '/system/payroll/' + id,
    method: 'delete'
  })
}

// 鏌ヨPayroll涓嬫媺鍒楄〃
export function listPayrollSelect(query) {
  return request({
    url: '/system/payroll/listBySelect',
    method: 'get',
    params: query
  })
}