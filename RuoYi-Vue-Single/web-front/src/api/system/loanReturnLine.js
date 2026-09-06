import request from '@/utils/request'

// 查询归还单-明细列表
export function listLoanReturnLine(query) {
  return request({
    url: '/system/loanReturnLine/list',
    method: 'get',
    params: query
  })
}

// 查询归还单-明细详细
export function getLoanReturnLine(id) {
  return request({
    url: '/system/loanReturnLine/' + id,
    method: 'get'
  })
}

// 新增归还单-明细
export function addLoanReturnLine(data) {
  return request({
    url: '/system/loanReturnLine',
    method: 'post',
    data: data
  })
}

// 修改归还单-明细
export function updateLoanReturnLine(data) {
  return request({
    url: '/system/loanReturnLine',
    method: 'put',
    data: data
  })
}

// 删除归还单-明细
export function delLoanReturnLine(id) {
  return request({
    url: '/system/loanReturnLine/' + id,
    method: 'delete'
  })
}
