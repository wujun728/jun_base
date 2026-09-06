import { add, get, list, remove, update } from '@/api/system/base'
import type { Entity, Query } from '@/api/system/types'

export type SafeId = string

export interface SqTransportTeam extends Entity {
  id: SafeId
  tname?: SafeId
  tnameLabel?: string
  mine?: SafeId
  mineLabel?: string
  tnameuser?: string
}

export interface SqTransportCar extends Entity {
  id: SafeId
  cars?: string
  carsNum?: string
  carsXh?: string
  mine?: SafeId
  mineLabel?: string
  carsComp?: SafeId
  carsCompLabel?: string
  carsW?: string
  teamid?: SafeId
  teamName?: string
  ctype?: string
  rfid?: string
  volume?: string
}

const teamRoot = '/business/sq/transport-team'
const carRoot = '/business/sq/transport-car'

export const fetchSqTransportTeamList = (params: Query) =>
  list<SqTransportTeam>(`${teamRoot}/list`, params)
export const fetchSqTransportTeamOptions = (params: Query = {}) =>
  get<SqTransportTeam[]>(`${teamRoot}/options`, params)
export const fetchSqTransportTeam = (id: SafeId | number) =>
  get<SqTransportTeam>(`${teamRoot}/${id}`)
export const addSqTransportTeam = (data: Entity) => add(teamRoot, data)
export const updateSqTransportTeam = (data: Entity) => update(teamRoot, data)
export const removeSqTransportTeam = (ids: SafeId | number) => remove(`${teamRoot}/${ids}`)

export const fetchSqTransportCarList = (params: Query) =>
  list<SqTransportCar>(`${carRoot}/list`, params)
export const fetchSqTransportCar = (id: SafeId | number) => get<SqTransportCar>(`${carRoot}/${id}`)
export const addSqTransportCar = (data: Entity) => add(carRoot, data)
export const updateSqTransportCar = (data: Entity) => update(carRoot, data)
export const removeSqTransportCar = (ids: SafeId | number) => remove(`${carRoot}/${ids}`)
