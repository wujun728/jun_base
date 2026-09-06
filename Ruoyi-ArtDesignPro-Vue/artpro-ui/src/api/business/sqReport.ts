import { get, list } from '@/api/system/base'
import type { Entity, Query } from '@/api/system/types'

export type SafeId = string

const reportRoot = '/business/sq/report'

export interface SqDispatchReport extends Entity {
  id: SafeId
  lname?: string
  company?: string
  theday?: string
  bigc?: string
  bigck?: string
  bigf?: string
  smailc?: string
  smailck?: string
  smailf?: string
}

export interface SqSafetyEventStat extends Entity {
  statDate?: string
  status?: string
  state?: string
  eventCount?: string
}

export const fetchSqDispatchReportList = (params: Query) =>
  list<SqDispatchReport>(`${reportRoot}/dispatch/list`, params)
export const fetchSqDispatchMonthReportList = (params: Query) =>
  list<SqDispatchReport>(`${reportRoot}/dispatch/month/list`, params)
export const fetchSqSafetyEventStatList = (params: Query) =>
  list<SqSafetyEventStat>(`${reportRoot}/safety-event/list`, params)
export const fetchSqDispatchReport = (id: SafeId | number) =>
  get<SqDispatchReport>(`${reportRoot}/dispatch/${id}`)

export const noopAction = async () => undefined
