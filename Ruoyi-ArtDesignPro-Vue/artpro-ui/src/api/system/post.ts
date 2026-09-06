import { add, get, list, remove, update } from './base'
import type { Entity, Query } from './types'
const root = '/system/post'
export const fetchPostList = (params: Query) => list(`${root}/list`, params)
export const fetchPost = (id: number) => get<Entity>(`${root}/${id}`)
export const addPost = (data: Entity) => add(root, data)
export const updatePost = (data: Entity) => update(root, data)
export const removePost = (ids: string | number) => remove(`${root}/${ids}`)
