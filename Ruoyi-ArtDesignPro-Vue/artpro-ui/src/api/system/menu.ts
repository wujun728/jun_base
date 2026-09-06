import { add, get, remove, update } from './base'
import type { Entity, Query, TreeNode } from './types'

const root = '/system/menu'
export const fetchMenuList = (params: Query = {}) => get<Entity[]>(`${root}/list`, params)
export const fetchMenu = (menuId: number) => get<Entity>(`${root}/${menuId}`)
export const fetchMenuTree = () => get<TreeNode[]>(`${root}/treeselect`)
export const fetchRoleMenuTree = (roleId: number) =>
  get<Entity>(`${root}/roleMenuTreeselect/${roleId}`)
export const addMenu = (data: Entity) => add(root, data)
export const updateMenu = (data: Entity) => update(root, data)
export const removeMenu = (menuId: number) => remove(`${root}/${menuId}`)
export const updateMenuSort = (menuIds: number[], orderNums: number[]) =>
  update(`${root}/updateSort`, { menuIds: menuIds.join(','), orderNums: orderNums.join(',') })
