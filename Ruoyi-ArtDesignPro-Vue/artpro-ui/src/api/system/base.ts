import request from '@/utils/http'
import type { Entity, PageResult, Query } from './types'

export const list = <T = Entity>(url: string, params: Query = {}) =>
  request.get<PageResult<T>>({ url, params })
export const get = <T = Entity>(url: string, params?: Query) => request.get<T>({ url, params })
export const add = <T = void>(url: string, data: Entity) =>
  request.post<T>({ url, data, showSuccessMessage: true })
export const update = <T = void>(url: string, data: Entity) =>
  request.put<T>({ url, data, showSuccessMessage: true })
export const remove = <T = void>(url: string) => request.del<T>({ url, showSuccessMessage: true })
export const queryPut = <T = void>(url: string, params: Query) =>
  request.request<T>({ url, method: 'PUT', params, data: {}, showSuccessMessage: true })
