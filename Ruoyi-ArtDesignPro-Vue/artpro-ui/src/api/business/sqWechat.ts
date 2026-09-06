import { add, get, list, remove, update } from '@/api/system/base'
import type { Entity, Query } from '@/api/system/types'

export type SafeId = string

export interface SqWxDept extends Entity {
  deptId: SafeId
  pid?: SafeId
  name?: string
  parentName?: string
  deptSort?: string
  subCount?: string
  enabled?: string
  type?: string
}

export interface SqWxUser extends Entity {
  userId: SafeId
  deptId?: SafeId
  deptName?: SafeId
  deptLabel?: string
  username?: string
  nickName?: string
}

export interface SqWxSend extends Entity {
  id: SafeId
  types?: number
  sendUser?: string
  isSend?: string
  level?: string
  tname?: string
  userName?: string
  deptId?: SafeId
  deptLabel?: string
}

const wxDeptRoot = '/business/sq/wx-dept'
const wxUserRoot = '/business/sq/wx-user'
const wxSendRoot = '/business/sq/wx-send'

export const fetchSqWxDeptList = (params: Query) => list<SqWxDept>(`${wxDeptRoot}/list`, params)
export const fetchSqWxDeptOptions = (params: Query = {}) =>
  get<SqWxDept[]>(`${wxDeptRoot}/options`, params)
export const fetchSqWxDept = (id: SafeId | number) => get<SqWxDept>(`${wxDeptRoot}/${id}`)
export const addSqWxDept = (data: Entity) => add(wxDeptRoot, data)
export const updateSqWxDept = (data: Entity) => update(wxDeptRoot, data)
export const removeSqWxDept = (ids: SafeId | number) => remove(`${wxDeptRoot}/${ids}`)

export const fetchSqWxUserList = (params: Query) => list<SqWxUser>(`${wxUserRoot}/list`, params)
export const fetchSqWxUser = (id: SafeId | number) => get<SqWxUser>(`${wxUserRoot}/${id}`)
export const addSqWxUser = (data: Entity) => add(wxUserRoot, data)
export const updateSqWxUser = (data: Entity) => update(wxUserRoot, data)
export const removeSqWxUser = (ids: SafeId | number) => remove(`${wxUserRoot}/${ids}`)

export const fetchSqWxSendList = (params: Query) => list<SqWxSend>(`${wxSendRoot}/list`, params)
export const fetchSqWxSend = (id: SafeId | number) => get<SqWxSend>(`${wxSendRoot}/${id}`)
export const addSqWxSend = (data: Entity) => add(wxSendRoot, data)
export const updateSqWxSend = (data: Entity) => update(wxSendRoot, data)
export const removeSqWxSend = (ids: SafeId | number) => remove(`${wxSendRoot}/${ids}`)
