import { add, get, list, queryPut, remove, update } from './base'
import type { Entity, Query } from './types'

const root = '/system/role'
export const fetchRoleList = (params: Query) => list(`${root}/list`, params)
export const fetchRole = (roleId: number) => get<Entity>(`${root}/${roleId}`)
export const addRole = (data: Entity) => add(root, data)
export const updateRole = (data: Entity) => update(root, data)
export const removeRole = (roleIds: string | number) => remove(`${root}/${roleIds}`)
export const changeRoleStatus = (data: Entity) => update(`${root}/changeStatus`, data)
export const updateRoleDataScope = (data: Entity) => update(`${root}/dataScope`, data)
export const fetchRoleDeptTree = (roleId: number) => get<Entity>(`${root}/deptTree/${roleId}`)
export const fetchAllocatedUsers = (params: Query) => list(`${root}/authUser/allocatedList`, params)
export const fetchUnallocatedUsers = (params: Query) =>
  list(`${root}/authUser/unallocatedList`, params)
export const cancelRoleUser = (data: Entity) => update(`${root}/authUser/cancel`, data)
export const cancelRoleUsers = (roleId: number, userIds: number[]) =>
  queryPut(`${root}/authUser/cancelAll`, { roleId, userIds })
export const selectRoleUsers = (roleId: number, userIds: number[]) =>
  queryPut(`${root}/authUser/selectAll`, { roleId, userIds })
