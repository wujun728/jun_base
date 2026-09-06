import request from '@/utils/http'
import { add, get, list, remove, update } from '@/api/system/base'
import type { Entity, Query } from '@/api/system/types'

const root = '/business/sq/chute'

export interface SqChute extends Entity {
  id: string
  shitype?: string
  shitypename?: string
  shitypeLabel?: string
  lname?: string
  company?: string
  tname?: string
  tnameLabel?: string
  mine?: string
  mineLabel?: string
  allQuantity?: string
  nowQuantity?: string
  ton?: string
  rad?: string
}

export const fetchSqChuteList = (params: Query) => list(`${root}/list`, params)
export const fetchSqChute = (id: string | number) => get<Entity>(`${root}/${id}`)
export const addSqChute = (data: Entity) => add(root, data)
export const updateSqChute = (data: Entity) => update(root, data)
export const removeSqChute = (ids: string | number) => remove(`${root}/${ids}`)
export const fetchSqChuteHeightList = (params: Query = {}) =>
  get<SqChute[]>(`${root}/height/list`, params)
export const updateSqChuteHeight = (data: Pick<SqChute, 'id' | 'nowQuantity'> & Entity) =>
  request.put<void>({ url: `${root}/height`, data, showSuccessMessage: true })
