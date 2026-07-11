import request from '@/utils/request'

// 查询租户管理列表
export function listTenant(query) {
  return request({
    url: '/system/tenant/list',
    method: 'get',
    params: query
  })
}

// 查询租户管理详细
export function getTenant(infoId) {
  return request({
    url: '/system/tenant/' + infoId,
    method: 'get'
  })
}

// 新增租户管理
export function addTenant(data) {
  return request({
    url: '/system/tenant',
    method: 'post',
    data: data
  })
}

// 修改租户管理
export function updateTenant(data) {
  return request({
    url: '/system/tenant',
    method: 'put',
    data: data
  })
}

// 删除租户管理
export function delTenant(infoId) {
  return request({
    url: '/system/tenant/' + infoId,
    method: 'delete'
  })
}

// 导出租户管理
export function exportTenant(query) {
  return request({
    url: '/system/tenant/export',
    method: 'get',
    params: query
  })
}
