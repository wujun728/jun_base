import request from '@/utils/request'

// 查询数据权限列表
export function listDataRule(query) {
  return request({
    url: '/system/dataRule/list',
    method: 'get',
    params: query
  })
}

// 查询数据权限详细
export function getDataRule(id) {
  return request({
    url: '/system/dataRule/' + id,
    method: 'get'
  })
}

// 新增数据权限
export function addDataRule(data) {
  return request({
    url: '/system/dataRule',
    method: 'post',
    data: data
  })
}

// 修改数据权限
export function updateDataRule(data) {
  return request({
    url: '/system/dataRule',
    method: 'put',
    data: data
  })
}

// 删除数据权限
export function delDataRule(id) {
  return request({
    url: '/system/dataRule/' + id,
    method: 'delete'
  })
}

// 导出数据权限
export function exportDataRule(query) {
  return request({
    url: '/system/dataRule/export',
    method: 'get',
    params: query
  })
}

export function updateRoleDataRules(data) {
  return request({
    url: '/system/dataRule/updateRules',
    method: 'post',
    data
  })
}

export function fetchRoleDataRuleList(data) {
  return request({
    url: '/system/dataRule/roleDataRuleList',
    method: 'get',
    params: data
  })
}
