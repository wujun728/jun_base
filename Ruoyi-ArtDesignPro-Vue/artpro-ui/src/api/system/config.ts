import request from '@/utils/http'
import { add, get, list, remove, update } from './base'
import type { Entity, Query } from './types'
const root = '/system/config'
export const fetchConfigList = (params: Query) => list(`${root}/list`, params)
export const fetchConfig = (id: number) => get<Entity>(`${root}/${id}`)
export const addConfig = (data: Entity) => add(root, data)
export const updateConfig = (data: Entity) => update(root, data)
export const removeConfig = (ids: string | number) => remove(`${root}/${ids}`)
export const refreshConfigCache = () => remove(`${root}/refreshCache`)
export const fetchSiteConfig = () => get<Record<string, string>>(`${root}/site`)
export const fetchConfigValue = (configKey: string) =>
  get<string>(`${root}/configKey/${encodeURIComponent(configKey)}`)
export const updateSiteConfig = (data: Record<string, string>) =>
  request.put<void>({ url: `${root}/site`, data })
export const fetchUserUiConfig = () => get<Record<string, string>>('/system/user-ui')
export const updateUserUiConfig = (data: Record<string, string>) =>
  request.put<void>({ url: '/system/user-ui', data })
