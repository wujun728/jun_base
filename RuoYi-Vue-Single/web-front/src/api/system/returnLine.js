import request from '@/utils/request'

// 查询退库单-明细列表
export function listReturnLine(query) {
  return request({
    url: '/system/returnLine/list',
    method: 'get',
    params: query
  })
}

// 查询退库单-明细详细
export function getReturnLine(id) {
  return request({
    url: '/system/returnLine/' + id,
    method: 'get'
  })
}

// 新增退库单-明细
export function addReturnLine(data) {
  return request({
    url: '/system/returnLine',
    method: 'post',
    data: data
  })
}

// 修改退库单-明细
export function updateReturnLine(data) {
  return request({
    url: '/system/returnLine',
    method: 'put',
    data: data
  })
}

// 删除退库单-明细
export function delReturnLine(id) {
  return request({
    url: '/system/returnLine/' + id,
    method: 'delete'
  })
}
