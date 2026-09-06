import request from '@/utils/request'

// 查询角色列表
export function listRole (query) {
  return request({
    url: '/system/role/list',
    method: 'get',
    params: query
  })
}

// 查询角色详细
export function getRole (roleId) {
  return request({
    url: '/system/role/' + roleId,
    method: 'get'
  })
}

// 新增角色
export function addRole (data) {
  return request({
    url: '/system/role',
    method: 'post',
    data: data
  })
}

// 修改角色
export function updateRole (data) {
  return request({
    url: '/system/role',
    method: 'put',
    data: data
  })
}

// 角色数据权限
export function dataScope (data) {
  return request({
    url: '/system/role/dataScope',
    method: 'put',
    data: data
  })
}

// 角色状态修改
export function changeRoleStatus (id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/system/role/changeStatus',
    method: 'put',
    data: data
  })
}

// 删除角色
export function delRole (roleId) {
  return request({
    url: '/system/role/' + roleId,
    method: 'delete'
  })
}

// 导出角色
export function exportRole (query) {
  return request({
    url: '/system/role/export',
    method: 'get',
    params: query
  })
}

// 校验角色名称唯一性
export function checkRoleNameUnique (data) {
  return request({
    url: '/system/role/checkRoleNameUnique',
    method: 'get',
    params: data
  })
}

// 校验角色名称唯一性
export function checkRoleKeyUnique (data) {
  return request({
    url: '/system/role/checkRoleKeyUnique',
    method: 'get',
    params: data
  })
}

// 查询最大排序
export function findMaxSort () {
  return request({
    url: '/system/role/findMaxSort',
    method: 'get'
  })
}
// 新增角色
export function batchSaveRole (data) {
  return request({
    url: '/system/role/batchSave',
    method: 'post',
    data: data
  })
}
// 删除角色
export function delRoleUser (roleId, userIds) {
  return request({
    url: '/system/role/deleteRoleUser/' + roleId + '/' + userIds,
    method: 'delete'
  })
}
// 给小页授权
export function saveRolePortlet (data) {
  return request({
    url: '/system/role/saveRolePortlet',
    method: 'post',
    data: data
  })
}
