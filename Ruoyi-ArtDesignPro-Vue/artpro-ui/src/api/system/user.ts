import request from '@/utils/http'
import { add, get, list, queryPut, remove, update } from './base'
import type { Entity, Query, TreeNode } from './types'

const root = '/system/user'
export const fetchUserList = (params: Query) => list(`${root}/list`, params)
export const fetchUser = (userId?: number) =>
  request.get<Entity>({ url: userId ? `${root}/${userId}` : `${root}/`, unwrapData: false })
export const addUser = (data: Entity) => add(root, data)
export const updateUser = (data: Entity) => update(root, data)
export const removeUser = (userIds: string | number) => remove(`${root}/${userIds}`)
export const changeUserStatus = (data: Entity) => update(`${root}/changeStatus`, data)
export const resetUserPassword = (data: Entity) => update(`${root}/resetPwd`, data)
export const fetchUserRoles = (userId: number) =>
  request.get<Entity>({ url: `${root}/authRole/${userId}`, unwrapData: false })
export const updateUserRoles = (userId: number, roleIds: number[]) =>
  queryPut(`${root}/authRole`, { userId, roleIds })
export const fetchDeptTree = () => get<TreeNode[]>(`${root}/deptTree`)
export function importUsers(file: File, updateSupport: boolean) {
  const data = new FormData()
  data.append('file', file)
  return request.post<void>({ url: `${root}/importData`, params: { updateSupport }, data })
}
