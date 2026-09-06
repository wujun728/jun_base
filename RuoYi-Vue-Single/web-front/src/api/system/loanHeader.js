import request from '@/utils/request'

// 查询借出单-主列表
export function listLoanHeader(query) {
  return request({
    url: '/system/loanHeader/list',
    method: 'get',
    params: query
  })
}

// 查询借出单-主详细
export function getLoanHeader(id) {
  return request({
    url: '/system/loanHeader/' + id,
    method: 'get'
  })
}

// 新增借出单-主
export function addLoanHeader(data) {
  return request({
    url: '/system/loanHeader',
    method: 'post',
    data: data
  })
}

// 修改借出单-主
export function updateLoanHeader(data) {
  return request({
    url: '/system/loanHeader',
    method: 'put',
    data: data
  })
}

// 删除借出单-主
export function delLoanHeader(id) {
  return request({
    url: '/system/loanHeader/' + id,
    method: 'delete'
  })
}
