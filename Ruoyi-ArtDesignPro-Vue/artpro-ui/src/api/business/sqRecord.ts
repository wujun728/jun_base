import request from '@/utils/http'
import { add, get, list, remove, update } from '@/api/system/base'
import type { Entity, Query } from '@/api/system/types'
import type { SqChute } from './sqChute'

export type SafeId = string

export interface SqFeedLog extends Entity {
  id: SafeId
  name?: SafeId
  chuteName?: string
  cars?: string
  fulls?: string
  bigs?: string
  yiwu?: string
  shitype?: string
  company?: string
  persons?: string
  forkcar?: string
  rate?: string
}

export interface SqOutLog extends Entity {
  id: SafeId
  ydNo?: string
  schedule?: string
  wellingPoint?: SafeId
  wellingPointName?: string
  mineralType?: string
  weighingTime?: string
  totalTonnage?: string
}

export interface SqLevelLog extends Entity {
  id: SafeId
  times?: number
  dates?: string
  lname?: SafeId
  chuteName?: string
  cars?: number
  heights?: string
}

export interface SqOfflineTask extends Entity {
  id: SafeId
  name?: string
  startTime?: string
  endTime?: string
  lname?: SafeId
  chuteName?: string
  channel?: string
  status?: string
}

const feedLogRoot = '/business/sq/feed-log'
const outLogRoot = '/business/sq/out-log'
const levelLogRoot = '/business/sq/level-log'
const offlineTaskRoot = '/business/sq/offline-task'
const feedOfflineLogRoot = '/business/sq/feed-offline-log'

export const fetchSqChuteOptions = (params: Query = {}) =>
  get<SqChute[]>('/business/sq/chute/options', params)

export const fetchSqFeedLogList = (params: Query) => list<SqFeedLog>(`${feedLogRoot}/list`, params)
export const fetchSqFeedLog = (id: SafeId | number) => get<SqFeedLog>(`${feedLogRoot}/${id}`)
export const addSqFeedLog = (data: Entity) => add(feedLogRoot, data)
export const updateSqFeedLog = (data: Entity) => update(feedLogRoot, data)
export const removeSqFeedLog = (ids: SafeId | number) => remove(`${feedLogRoot}/${ids}`)

export const fetchSqOutLogList = (params: Query) => list<SqOutLog>(`${outLogRoot}/list`, params)
export const fetchSqOutLog = (id: SafeId | number) => get<SqOutLog>(`${outLogRoot}/${id}`)
export const addSqOutLog = (data: Entity) => add(outLogRoot, data)
export const updateSqOutLog = (data: Entity) => update(outLogRoot, data)
export const removeSqOutLog = (ids: SafeId | number) => remove(`${outLogRoot}/${ids}`)

export const fetchSqLevelLogList = (params: Query) =>
  list<SqLevelLog>(`${levelLogRoot}/list`, params)
export const fetchSqLevelLog = (id: SafeId | number) => get<SqLevelLog>(`${levelLogRoot}/${id}`)
export const addSqLevelLog = (data: Entity) => add(levelLogRoot, data)
export const updateSqLevelLog = (data: Entity) => update(levelLogRoot, data)
export const removeSqLevelLog = (ids: SafeId | number) => remove(`${levelLogRoot}/${ids}`)

export const fetchSqOfflineTaskList = (params: Query) =>
  list<SqOfflineTask>(`${offlineTaskRoot}/list`, params)
export const fetchSqOfflineTask = (id: SafeId | number) =>
  get<SqOfflineTask>(`${offlineTaskRoot}/${id}`)
export const addSqOfflineTask = (data: Entity) => add(offlineTaskRoot, data)
export const updateSqOfflineTask = (data: Entity) => update(offlineTaskRoot, data)
export const updateSqOfflineTaskStatus = (data: Entity) =>
  request.put<void>({ url: `${offlineTaskRoot}/status`, data, showSuccessMessage: true })
export const removeSqOfflineTask = (ids: SafeId | number) => remove(`${offlineTaskRoot}/${ids}`)

export const fetchSqFeedOfflineLogList = (params: Query) =>
  list<SqFeedLog>(`${feedOfflineLogRoot}/list`, params)
export const fetchSqFeedOfflineLog = (id: SafeId | number) =>
  get<SqFeedLog>(`${feedOfflineLogRoot}/${id}`)
export const addSqFeedOfflineLog = (data: Entity) => add(feedOfflineLogRoot, data)
export const updateSqFeedOfflineLog = (data: Entity) => update(feedOfflineLogRoot, data)
export const removeSqFeedOfflineLog = (ids: SafeId | number) =>
  remove(`${feedOfflineLogRoot}/${ids}`)
