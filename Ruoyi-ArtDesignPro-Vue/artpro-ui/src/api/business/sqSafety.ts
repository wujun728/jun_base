import { add, get, list, remove, update } from '@/api/system/base'
import type { Entity, Query } from '@/api/system/types'

export type SafeId = string

const safetyAlertRoot = '/business/sq/safety-alert'
const userBehaviorRoot = '/business/sq/user-behavior'
const inclinedShaftRoot = '/business/sq/inclined-shaft'
const sirenRoot = '/business/sq/siren'
const alertInfoRoot = '/business/sq/alert-info'

export const fetchSqSafetyAlertList = (params: Query) => list(`${safetyAlertRoot}/list`, params)
export const fetchSqSafetyAlert = (id: SafeId | number) => get(`${safetyAlertRoot}/${id}`)
export const addSqSafetyAlert = (data: Entity) => add(safetyAlertRoot, data)
export const updateSqSafetyAlert = (data: Entity) => update(safetyAlertRoot, data)
export const removeSqSafetyAlert = (ids: SafeId | number) => remove(`${safetyAlertRoot}/${ids}`)

export const fetchSqUserBehaviorList = (params: Query) => list(`${userBehaviorRoot}/list`, params)
export const fetchSqUserBehavior = (id: SafeId | number) => get(`${userBehaviorRoot}/${id}`)
export const addSqUserBehavior = (data: Entity) => add(userBehaviorRoot, data)
export const updateSqUserBehavior = (data: Entity) => update(userBehaviorRoot, data)
export const removeSqUserBehavior = (ids: SafeId | number) => remove(`${userBehaviorRoot}/${ids}`)

export const fetchSqInclinedShaftList = (params: Query) => list(`${inclinedShaftRoot}/list`, params)
export const fetchSqInclinedShaft = (id: SafeId | number) => get(`${inclinedShaftRoot}/${id}`)
export const addSqInclinedShaft = (data: Entity) => add(inclinedShaftRoot, data)
export const updateSqInclinedShaft = (data: Entity) => update(inclinedShaftRoot, data)
export const removeSqInclinedShaft = (ids: SafeId | number) => remove(`${inclinedShaftRoot}/${ids}`)

export const fetchSqSirenList = (params: Query) => list(`${sirenRoot}/list`, params)
export const fetchSqSiren = (id: SafeId | number) => get(`${sirenRoot}/${id}`)
export const addSqSiren = (data: Entity) => add(sirenRoot, data)
export const updateSqSiren = (data: Entity) => update(sirenRoot, data)
export const removeSqSiren = (ids: SafeId | number) => remove(`${sirenRoot}/${ids}`)

export const fetchSqAlertInfoList = (params: Query) => list(`${alertInfoRoot}/list`, params)
export const fetchSqAlertInfo = (id: SafeId | number) => get(`${alertInfoRoot}/${id}`)
export const addSqAlertInfo = (data: Entity) => add(alertInfoRoot, data)
export const updateSqAlertInfo = (data: Entity) => update(alertInfoRoot, data)
export const removeSqAlertInfo = (ids: SafeId | number) => remove(`${alertInfoRoot}/${ids}`)
