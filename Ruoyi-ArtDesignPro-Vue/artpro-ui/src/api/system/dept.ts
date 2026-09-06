import { add, get, remove, update } from './base'
import type { Entity, Query } from './types'

const root = '/system/dept'
export const fetchDeptList = (params: Query = {}) => get<Entity[]>(`${root}/list`, params)
export const fetchDept = (deptId: number) => get<Entity>(`${root}/${deptId}`)
export const fetchDeptExclude = (deptId: number) => get<Entity[]>(`${root}/list/exclude/${deptId}`)
export const addDept = (data: Entity) => add(root, data)
export const updateDept = (data: Entity) => update(root, data)
export const removeDept = (deptId: number) => remove(`${root}/${deptId}`)
export const updateDeptSort = (deptIds: number[], orderNums: number[]) =>
  update(`${root}/updateSort`, { deptIds: deptIds.join(','), orderNums: orderNums.join(',') })
