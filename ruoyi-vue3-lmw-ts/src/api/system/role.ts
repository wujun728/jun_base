import { request } from '@/utils/hsj/service/index'

// 查询角色详细
export function getRole(roleId: number) {
  return request({
    url: '/system/role/' + roleId,
    method: 'get',
  })
}

// 角色数据权限
export function dataScope(data: anyObj) {
  return request({
    url: '/system/role/dataScope',
    method: 'put',
    data: data,
  })
}

// 角色状态修改
export function changeRoleStatus(roleId: number, status: StrNum) {
  const data = {
    roleId,
    status,
  }
  return request({
    url: '/system/role/changeStatus',
    method: 'put',
    data: data,
  })
}

// 取消用户授权角色
export function authUserCancel(data: anyObj) {
  return request({
    url: '/system/role/authUser/cancel',
    method: 'put',
    data: data,
  })
}

// 批量取消用户授权角色
export function authUserCancelAll(data: anyObj) {
  return request({
    url: '/system/role/authUser/cancelAll',
    method: 'put',
    params: data,
  })
}

// 授权用户选择
export function authUserSelectAll(data: anyObj) {
  return request({
    url: '/system/role/authUser/selectAll',
    method: 'put',
    params: data,
  })
}

// 根据角色ID查询部门树结构
export function deptTreeSelect(roleId: StrNum) {
  return request({
    url: '/system/role/deptTree/' + roleId,
    method: 'get',
  })
}
