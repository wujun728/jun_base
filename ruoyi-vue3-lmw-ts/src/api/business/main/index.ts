import request from '@/utils/hsj/service/index.ts'
import { interceptorsMes, interceptorsData } from '../interceptors/index'
import { mainBase } from '../config'

export function getPageListData(url: string, params: anyObj) {
  return request.get({
    url: mainBase + url,
    params,
    interceptors: interceptorsMes,
  })
}

export function deletData(url: string) {
  return request.delete({
    url: mainBase + url,
    interceptors: interceptorsMes,
  })
}

export function editData(url: string, editData: anyObj) {
  return request.put({
    url: mainBase + url,
    data: editData,
    interceptors: interceptorsMes,
  })
}

export function createData(url: string, newData: anyObj) {
  return request.post({
    url: mainBase + url,
    data: newData,
    interceptors: interceptorsMes,
  })
}

export function getInfo(url: string) {
  return request.get({
    url: mainBase + url,
    interceptors: interceptorsData,
  })
}
