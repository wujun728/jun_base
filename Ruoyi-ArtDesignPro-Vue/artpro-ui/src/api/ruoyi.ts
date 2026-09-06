import request from '@/utils/http'

export type QueryParams = Record<string, unknown>
export type Entity = Record<string, any>

export function list(url: string, params: QueryParams = {}) {
  return request.get<{ rows: Entity[]; total: number }>({ url, params })
}

export function get<T = Entity>(url: string) {
  return request.get<T>({ url })
}

export function add(url: string, data: Entity) {
  return request.post<void>({ url, params: data, showSuccessMessage: true })
}

export function update(url: string, data: Entity) {
  return request.put<void>({ url, params: data, showSuccessMessage: true })
}

export function remove(url: string) {
  return request.del<void>({ url, showSuccessMessage: true })
}

export function changeStatus(url: string, data: Entity) {
  return request.put<void>({ url, params: data, showSuccessMessage: true })
}
