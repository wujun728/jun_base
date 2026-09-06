import request from '@/utils/http'
import { downloadByGet } from '@/api/common'
import type { Entity, PageResult, Query } from '@/api/system/types'

export interface GenInfo {
  info: Entity
  rows: Entity[]
  tables: Entity[]
}

export const fetchGenList = (params: Query) =>
  request.get<PageResult>({ url: '/tool/gen/list', params })
export const fetchDbTables = (params: Query) =>
  request.get<PageResult>({ url: '/tool/gen/db/list', params })
export const fetchGenInfo = (tableId: number) =>
  request.get<GenInfo>({ url: `/tool/gen/${tableId}` })
export const importTables = (tables: string, tplWebType: string) =>
  request.request<void>({
    url: '/tool/gen/importTable',
    method: 'POST',
    params: { tables, tplWebType },
    data: {},
    showSuccessMessage: true
  })
export const updateGen = (data: Entity) =>
  request.put<void>({ url: '/tool/gen', data, showSuccessMessage: true })
export const removeGen = (tableIds: string | number) =>
  request.del<void>({ url: `/tool/gen/${tableIds}`, showSuccessMessage: true })
export const previewGen = (tableId: number) =>
  request.get<Record<string, string>>({ url: `/tool/gen/preview/${tableId}` })
export const syncGen = (tableName: string) =>
  request.get<void>({ url: `/tool/gen/synchDb/${tableName}`, showSuccessMessage: true })
export const generateGen = (tableName: string) =>
  request.get<void>({ url: `/tool/gen/genCode/${tableName}`, showSuccessMessage: true })
export const downloadGen = (tableName: string) =>
  downloadByGet(`/tool/gen/download/${tableName}`, {}, `${tableName}.zip`)
export const downloadBatchGen = (tables: string) =>
  downloadByGet('/tool/gen/batchGenCode', { tables }, 'ruoyi.zip')
