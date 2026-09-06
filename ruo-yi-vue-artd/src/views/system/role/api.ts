import type { AuthUserQueryParams, SysUser } from '@/types/api/system/user'

import {
  TreeSelect,
  type AjaxResult,
  type TableDataInfo
} from '@/types/api/system/common'
import { RoleMenuTreeselectResult } from '@/types/api/system/menu'
import {
  RoleQueryParams,
  SysRole,
  type AuthUserSelectParams,
  type RoleDeptTreeResult,
  type SysUserRole
} from '@/types/api/system/role'
import response from '@/utils/http'

// 查询角色列表
export function listRole(query: RoleQueryParams) {
  return response<SysRole[]>({
    url: '/system/role/list',
    method: 'get',
    params: query
  })
}

// 查询菜单下拉树结构
export function menuTreeselect() {
  return response<TreeSelect[]>({
    url: '/system/menu/treeselect',
    method: 'get'
  })
}

// 查询角色详细
export function getRole(roleId: number) {
  return response<SysRole>({
    url: '/system/role/' + roleId,
    method: 'get'
  })
}

// 新增角色
export function addRole(data: SysRole) {
  return response({
    url: '/system/role',
    method: 'post',
    data: data
  })
}

// 修改角色
export function updateRole(data: SysRole) {
  return response({
    url: '/system/role',
    method: 'put',
    data: data
  })
}

// // 角色数据权限
// export function dataScope(data: SysRole): Promise<AjaxResult> {
//   return request({
//     url: '/system/role/dataScope',
//     method: 'put',
//     data: data
//   })
// }

// 角色数据权限
export function dataScope(data: SysRole) {
  return response({
    url: '/system/role/dataScope',
    method: 'put',
    data: data
  })
}

// 角色状态修改
export function changeRoleStatus(roleId: number, status: string) {
  return response({
    url: '/system/role/changeStatus',
    method: 'put',
    data: {
      roleId,
      status
    }
  })
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeselect(roleId: number) {
  return response<RoleMenuTreeselectResult>({
    url: '/system/menu/roleMenuTreeselect/' + roleId,
    method: 'get'
  })
}
// 删除角色
export function delRole(roleId: number | number[]) {
  return response({
    url: '/system/role/' + roleId,
    method: 'delete'
  })
}

// 查询角色已授权用户列表
export function allocatedUserList(query: AuthUserQueryParams) {
  return response<TableDataInfo<SysUser>>({
    url: '/system/role/authUser/allocatedList',
    method: 'get',
    params: query
  })
}

// 查询角色未授权用户列表
export function unallocatedUserList(query: AuthUserQueryParams) {
  return response<TableDataInfo<SysUser>>({
    url: '/system/role/authUser/unallocatedList',
    method: 'get',
    params: query
  })
}

// 取消用户授权角色
export function authUserCancel(data: SysUserRole) {
  return response({
    url: '/system/role/authUser/cancel',
    method: 'put',
    data: data
  })
}

// 批量取消用户授权角色
export function authUserCancelAll(data: AuthUserSelectParams) {
  return response({
    url: '/system/role/authUser/cancelAll',
    method: 'put',
    params: data
  })
}

// 授权用户选择
export function authUserSelectAll(data: AuthUserSelectParams) {
  return response<AjaxResult>({
    url: '/system/role/authUser/selectAll',
    method: 'put',
    params: data
  })
}

// 根据角色ID查询部门树结构
export function deptTreeSelect(roleId: number) {
  return response<RoleDeptTreeResult>({
    url: '/system/role/deptTree/' + roleId,
    method: 'get'
  })
}
