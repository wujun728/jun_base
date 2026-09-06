import request from '@/utils/request'

// 查询盘点单-主列表
export function listStockCountHeader(query) {
  return request({
    url: '/system/stockCountHeader/list',
    method: 'get',
    params: query
  })
}

// 查询盘点单-主详细
export function getStockCountHeader(id) {
  return request({
    url: '/system/stockCountHeader/' + id,
    method: 'get'
  })
}

// 新增盘点单-主
export function addStockCountHeader(data) {
  return request({
    url: '/system/stockCountHeader',
    method: 'post',
    data: data
  })
}

// 修改盘点单-主
export function updateStockCountHeader(data) {
  return request({
    url: '/system/stockCountHeader',
    method: 'put',
    data: data
  })
}

// 删除盘点单-主
export function delStockCountHeader(id) {
  return request({
    url: '/system/stockCountHeader/' + id,
    method: 'delete'
  })
}
