import request from '@/utils/request'

// 查询库存现势(按仓库+产品)列表
export function listInventoryBalance(query) {
  return request({
    url: '/system/inventoryBalance/list',
    method: 'get',
    params: query
  })
}

// 查询库存现势(按仓库+产品)详细
export function getInventoryBalance(id) {
  return request({
    url: '/system/inventoryBalance/' + id,
    method: 'get'
  })
}

// 新增库存现势(按仓库+产品)
export function addInventoryBalance(data) {
  return request({
    url: '/system/inventoryBalance',
    method: 'post',
    data: data
  })
}

// 修改库存现势(按仓库+产品)
export function updateInventoryBalance(data) {
  return request({
    url: '/system/inventoryBalance',
    method: 'put',
    data: data
  })
}

// 删除库存现势(按仓库+产品)
export function delInventoryBalance(id) {
  return request({
    url: '/system/inventoryBalance/' + id,
    method: 'delete'
  })
}
