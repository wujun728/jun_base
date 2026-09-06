import request from '@/utils/request'

// 查询借出单-明细列表
export function listLoanLine(query) {
  return request({
    url: '/system/loanLine/list',
    method: 'get',
    params: query
  })
}

// 查询借出单-明细详细
export function getLoanLine(id) {
  return request({
    url: '/system/loanLine/' + id,
    method: 'get'
  })
}

// 新增借出单-明细
export function addLoanLine(data) {
  return request({
    url: '/system/loanLine',
    method: 'post',
    data: data
  })
}

// 修改借出单-明细
export function updateLoanLine(data) {
  return request({
    url: '/system/loanLine',
    method: 'put',
    data: data
  })
}

// 删除借出单-明细
export function delLoanLine(id) {
  return request({
    url: '/system/loanLine/' + id,
    method: 'delete'
  })
}
