import { add, get, list, remove, update } from './base'
import type { Entity, Query } from './types'
export const fetchDictTypeList = (params: Query) => list('/system/dict/type/list', params)
export const fetchDictType = (id: number) => get<Entity>(`/system/dict/type/${id}`)
export const addDictType = (data: Entity) => add('/system/dict/type', data)
export const updateDictType = (data: Entity) => update('/system/dict/type', data)
export const removeDictType = (ids: string | number) => remove(`/system/dict/type/${ids}`)
export const refreshDictCache = () => remove('/system/dict/type/refreshCache')
export const fetchDictDataList = (params: Query) => list('/system/dict/data/list', params)
export const fetchDictDatum = (id: number) => get<Entity>(`/system/dict/data/${id}`)
export const addDictDatum = (data: Entity) => add('/system/dict/data', data)
export const updateDictDatum = (data: Entity) => update('/system/dict/data', data)
export const removeDictDatum = (ids: string | number) => remove(`/system/dict/data/${ids}`)
