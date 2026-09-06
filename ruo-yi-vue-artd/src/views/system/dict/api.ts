import type { TableDataInfo } from '@/types/api/system/common'
import type {
  DictDataQueryParams,
  DictTypeQueryParams,
  SysDictData,
  SysDictType
} from '@/types/api/system/dict'

import response from '@/utils/http'

// 查询字典类型列表
export function listType(query: DictTypeQueryParams) {
  return response<TableDataInfo<SysDictType>>({
    url: '/system/dict/type/list',
    method: 'get',
    params: query
  })
}

// 查询字典类型详细
export function getType(dictId: number) {
  return response<SysDictType>({
    url: '/system/dict/type/' + dictId,
    method: 'get'
  })
}

// 新增字典类型
export function addType(data: SysDictType) {
  return response({
    url: '/system/dict/type',
    method: 'post',
    data
  })
}

// 修改字典类型
export function updateType(data: SysDictType) {
  return response({
    url: '/system/dict/type',
    method: 'put',
    data
  })
}

// 删除字典类型
export function delType(dictId: number | number[]) {
  return response({
    url: '/system/dict/type/' + dictId,
    method: 'delete'
  })
}

// 刷新字典缓存
export function refreshCache() {
  return response({
    url: '/system/dict/type/refreshCache',
    method: 'delete'
  })
}

// 查询字典类型选项
export function optionselect() {
  return response<SysDictType[]>({
    url: '/system/dict/type/optionselect',
    method: 'get'
  })
}

// 查询字典数据列表
export function listData(query: DictDataQueryParams) {
  return response<TableDataInfo<SysDictData>>({
    url: '/system/dict/data/list',
    method: 'get',
    params: query
  })
}

// 查询字典数据详细
export function getData(dictCode: number) {
  return response<SysDictData>({
    url: '/system/dict/data/' + dictCode,
    method: 'get'
  })
}

// 新增字典数据
export function addData(data: SysDictData) {
  return response({
    url: '/system/dict/data',
    method: 'post',
    data
  })
}

// 修改字典数据
export function updateData(data: SysDictData) {
  return response({
    url: '/system/dict/data',
    method: 'put',
    data
  })
}

// 删除字典数据
export function delData(dictCode: number | number[]) {
  return response({
    url: '/system/dict/data/' + dictCode,
    method: 'delete'
  })
}
