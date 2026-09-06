import request from '@/utils/request'

// 查询采购进货单-主列表
export function listGrnHeader(query) {
  return request({
    url: '/system/grnHeader/list',
    method: 'get',
    params: query
  })
}

// 查询采购进货单-主详细
export function getGrnHeader(id) {
  return request({
    url: '/system/grnHeader/' + id,
    method: 'get'
  })
}

// 新增采购进货单-主
export function addGrnHeader(data) {
  return request({
    url: '/system/grnHeader',
    method: 'post',
    data: data
  })
}

// 修改采购进货单-主
export function updateGrnHeader(data) {
  return request({
    url: '/system/grnHeader',
    method: 'put',
    data: data
  })
}

// 删除采购进货单-主
export function delGrnHeader(id) {
  return request({
    url: '/system/grnHeader/' + id,
    method: 'delete'
  })
}
