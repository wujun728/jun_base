import request from '@/utils/request'

// 查询盘点单-明细列表
export function listStockCountLine(query) {
  return request({
    url: '/system/stockCountLine/list',
    method: 'get',
    params: query
  })
}

// 查询盘点单-明细详细
export function getStockCountLine(id) {
  return request({
    url: '/system/stockCountLine/' + id,
    method: 'get'
  })
}

// 新增盘点单-明细
export function addStockCountLine(data) {
  return request({
    url: '/system/stockCountLine',
    method: 'post',
    data: data
  })
}

// 修改盘点单-明细
export function updateStockCountLine(data) {
  return request({
    url: '/system/stockCountLine',
    method: 'put',
    data: data
  })
}

// 删除盘点单-明细
export function delStockCountLine(id) {
  return request({
    url: '/system/stockCountLine/' + id,
    method: 'delete'
  })
}
