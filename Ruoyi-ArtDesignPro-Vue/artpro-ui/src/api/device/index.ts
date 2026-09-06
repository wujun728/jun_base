import { add, get, list, remove, update } from '@/api/system/base'
import type { Entity, Query } from '@/api/system/types'

export const fetchChuteOptions = () => get<Entity[]>('/device/chute/options')
export const addChute = (data: Entity) => add<Entity>('/device/chute', data)

const cameraRoot = '/device/camera'
export const fetchCameraList = (params: Query) => list(`${cameraRoot}/list`, params)
export const fetchCamera = (id: number) => get<Entity>(`${cameraRoot}/${id}`)
export const addCamera = (data: Entity) => add(cameraRoot, data)
export const updateCamera = (data: Entity) => update(cameraRoot, data)
export const removeCamera = (ids: string | number) => remove(`${cameraRoot}/${ids}`)

const alarmRoot = '/device/alarm'
export const fetchAlarmList = (params: Query) => list(`${alarmRoot}/list`, params)
export const fetchAlarm = (id: number) => get<Entity>(`${alarmRoot}/${id}`)
export const addAlarm = (data: Entity) => add(alarmRoot, data)
export const updateAlarm = (data: Entity) => update(alarmRoot, data)
export const removeAlarm = (ids: string | number) => remove(`${alarmRoot}/${ids}`)

const readerRoot = '/device/reader'
export const fetchReaderList = (params: Query) => list(`${readerRoot}/list`, params)
export const fetchReader = (id: number) => get<Entity>(`${readerRoot}/${id}`)
export const addReader = (data: Entity) => add(readerRoot, data)
export const updateReader = (data: Entity) => update(readerRoot, data)
export const removeReader = (ids: string | number) => remove(`${readerRoot}/${ids}`)
