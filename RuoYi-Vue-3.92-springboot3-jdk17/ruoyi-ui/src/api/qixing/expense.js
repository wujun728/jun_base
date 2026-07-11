import request from '@/utils/request'

// 查询费用报销列表
export function listExpense(query) {
  return request({
    url: '/system/expense/list',
    method: 'get',
    params: query
  })
}

// 查询费用报销详细
export function getExpense(id) {
  return request({
    url: '/system/expense/' + id,
    method: 'get'
  })
}

// 新增费用报销
export function addExpense(data) {
  return request({
    url: '/system/expense',
    method: 'post',
    data: data
  })
}

// 修改费用报销
export function updateExpense(data) {
  return request({
    url: '/system/expense',
    method: 'put',
    data: data
  })
}

// 删除费用报销
export function delExpense(id) {
  return request({
    url: '/system/expense/' + id,
    method: 'delete'
  })
}

// 鏌ヨExpense涓嬫媺鍒楄〃
export function listExpenseSelect(query) {
  return request({
    url: '/system/expense/listBySelect',
    method: 'get',
    params: query
  })
}