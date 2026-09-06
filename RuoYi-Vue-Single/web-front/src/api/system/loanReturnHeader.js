import request from '@/utils/request'

// 查询归还单-主列表
export function listLoanReturnHeader(query) {
  return request({
    url: '/system/loanReturnHeader/list',
    method: 'get',
    params: query
  })
}

// 查询归还单-主详细
export function getLoanReturnHeader(id) {
  return request({
    url: '/system/loanReturnHeader/' + id,
    method: 'get'
  })
}

// 新增归还单-主
export function addLoanReturnHeader(data) {
  return request({
    url: '/system/loanReturnHeader',
    method: 'post',
    data: data
  })
}

// 修改归还单-主
export function updateLoanReturnHeader(data) {
  return request({
    url: '/system/loanReturnHeader',
    method: 'put',
    data: data
  })
}

// 删除归还单-主
export function delLoanReturnHeader(id) {
  return request({
    url: '/system/loanReturnHeader/' + id,
    method: 'delete'
  })
}
