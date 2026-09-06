import request from '@/utils/http'
import type { Entity, PageResult, Query } from '@/api/system/types'

const page = <T = Entity>(url: string, params: Query = {}) =>
  request.get<PageResult<T>>({ url, params })
const get = <T = Entity>(url: string, params?: Query) => request.get<T>({ url, params })
const del = (url: string) => request.del<void>({ url, showSuccessMessage: true })
const post = (url: string, data: Entity) =>
  request.post<void>({ url, data, showSuccessMessage: true })
const put = (url: string, data: Entity) =>
  request.put<void>({ url, data, showSuccessMessage: true })

export const fetchOnlineList = (params: Query) => page('/monitor/online/list', params)
export const forceLogout = (tokenId: string | number) => del(`/monitor/online/${tokenId}`)
export const cleanOnlineUsers = () => del('/monitor/online/clean')

export const fetchJobList = (params: Query) => page('/monitor/job/list', params)
export const fetchJob = (id: number) => get<Entity>(`/monitor/job/${id}`)
export const addJob = (data: Entity) => post('/monitor/job', data)
export const updateJob = (data: Entity) => put('/monitor/job', data)
export const removeJob = (ids: string | number) => del(`/monitor/job/${ids}`)
export const changeJobStatus = (data: Entity) => put('/monitor/job/changeStatus', data)
export const runJob = (data: Entity) => put('/monitor/job/run', data)

export const fetchJobLogList = (params: Query) => page('/monitor/jobLog/list', params)
export const fetchJobLog = (id: number) => get<Entity>(`/monitor/jobLog/${id}`)
export const removeJobLog = (ids: string | number) => del(`/monitor/jobLog/${ids}`)
export const cleanJobLogs = () => del('/monitor/jobLog/clean')

export const fetchOperLogList = (params: Query) => page('/monitor/operlog/list', params)
export const removeOperLog = (ids: string | number) => del(`/monitor/operlog/${ids}`)
export const cleanOperLogs = () => del('/monitor/operlog/clean')

export const fetchLogininforList = (params: Query) => page('/monitor/logininfor/list', params)
export const removeLogininfor = (ids: string | number) => del(`/monitor/logininfor/${ids}`)
export const cleanLogininfors = () => del('/monitor/logininfor/clean')
export const unlockUser = (userName: string) => get(`/monitor/logininfor/unlock/${userName}`)

export const fetchServerInfo = () => get('/monitor/server')
export const fetchCacheInfo = () => get('/monitor/cache')
export const fetchCacheNames = () => get<Entity[]>('/monitor/cache/getNames')
export const fetchCacheKeys = (cacheName: string) =>
  get<string[]>(`/monitor/cache/getKeys/${encodeURIComponent(cacheName)}`)
export const fetchCacheValue = (cacheName: string, cacheKey: string) =>
  get<Entity>(
    `/monitor/cache/getValue/${encodeURIComponent(cacheName)}/${encodeURIComponent(cacheKey)}`
  )
export const clearCacheName = (cacheName: string) =>
  del(`/monitor/cache/clearCacheName/${encodeURIComponent(cacheName)}`)
export const clearCacheKey = (cacheKey: string) =>
  del(`/monitor/cache/clearCacheKey/${encodeURIComponent(cacheKey)}`)
export const clearCacheAll = () => del('/monitor/cache/clearCacheAll')
