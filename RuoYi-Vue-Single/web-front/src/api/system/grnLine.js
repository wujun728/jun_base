import request from '@/utils/request'

// 查询采购进货单-明细列表
export function listGrnLine(query) {
  return request({
    url: '/system/grnLine/list',
    method: 'get',
    params: query
  })
}

// 查询采购进货单-明细详细
export function getGrnLine(id) {
  return request({
    url: '/system/grnLine/' + id,
    method: 'get'
  })
}

// 新增采购进货单-明细
export function addGrnLine(data) {
  return request({
    url: '/system/grnLine',
    method: 'post',
    data: data
  })
}

// 修改采购进货单-明细
export function updateGrnLine(data) {
  return request({
    url: '/system/grnLine',
    method: 'put',
    data: data
  })
}

// 删除采购进货单-明细
export function delGrnLine(id) {
  return request({
    url: '/system/grnLine/' + id,
    method: 'delete'
  })
}
