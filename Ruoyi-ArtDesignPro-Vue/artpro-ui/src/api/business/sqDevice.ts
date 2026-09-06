import { add, get, list, remove, update } from '@/api/system/base'
import type { Entity, Query } from '@/api/system/types'
import type { SqChute } from './sqChute'

export type SafeId = string

export interface SqRfidReader extends Entity {
  id: SafeId
  location?: string
  status?: string
  mark?: string
  name?: string
  ip?: string
  username?: string
  psw?: string
  port?: string
  lUserid?: string
  tname?: SafeId
  tnameLabel?: string
}

export interface SqDeviceRecord extends Entity {
  id: SafeId
  channelId?: string
  lname?: string
  isOpen?: string
  carmip?: string
  type?: string
}

const deviceCameraRoot = '/business/sq/device-camera'
const rfidReaderRoot = '/business/sq/rfid-reader'
const deviceSirenRoot = '/business/sq/device-siren'
const deviceRecordRoot = '/business/sq/device-record'

export const fetchSqDeviceCameraList = (params: Query) => list(`${deviceCameraRoot}/list`, params)
export const fetchSqDeviceCamera = (id: SafeId | number) => get(`${deviceCameraRoot}/${id}`)
export const addSqDeviceCamera = (data: Entity) => add(deviceCameraRoot, data)
export const updateSqDeviceCamera = (data: Entity) => update(deviceCameraRoot, data)
export const removeSqDeviceCamera = (ids: SafeId | number) => remove(`${deviceCameraRoot}/${ids}`)

export const fetchSqDeviceCameraOptions = (params: Query = {}) =>
  get<SqChute[]>('/business/sq/chute/options', params)

export const fetchSqRfidReaderList = (params: Query) => list(`${rfidReaderRoot}/list`, params)
export const fetchSqRfidReader = (id: SafeId | number) =>
  get<SqRfidReader>(`${rfidReaderRoot}/${id}`)
export const addSqRfidReader = (data: Entity) => add(rfidReaderRoot, data)
export const updateSqRfidReader = (data: Entity) => update(rfidReaderRoot, data)
export const removeSqRfidReader = (ids: SafeId | number) => remove(`${rfidReaderRoot}/${ids}`)

export const fetchSqDeviceSirenList = (params: Query) => list(`${deviceSirenRoot}/list`, params)
export const fetchSqDeviceSiren = (id: SafeId | number) => get(`${deviceSirenRoot}/${id}`)
export const addSqDeviceSiren = (data: Entity) => add(deviceSirenRoot, data)
export const updateSqDeviceSiren = (data: Entity) => update(deviceSirenRoot, data)
export const removeSqDeviceSiren = (ids: SafeId | number) => remove(`${deviceSirenRoot}/${ids}`)

export const fetchSqDeviceRecordList = (params: Query) => list(`${deviceRecordRoot}/list`, params)
export const fetchSqDeviceRecord = (id: SafeId | number) =>
  get<SqDeviceRecord>(`${deviceRecordRoot}/${id}`)
export const addSqDeviceRecord = (data: Entity) => add(deviceRecordRoot, data)
export const updateSqDeviceRecord = (data: Entity) => update(deviceRecordRoot, data)
export const removeSqDeviceRecord = (ids: SafeId | number) => remove(`${deviceRecordRoot}/${ids}`)
