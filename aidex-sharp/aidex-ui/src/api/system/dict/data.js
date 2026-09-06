import request from '@/utils/request'

// 查询字典数据列表
export function listData (query) {
  return request({
    url: '/system/dict/data/page',
    method: 'get',
    params: query
  })
}

// 查询字典数据详细
export function getData (dictCode) {
  return request({
    url: '/system/dict/data/' + dictCode,
    method: 'get'
  })
}

// 根据字典类型查询字典数据信息
export function getDicts (dictType) {
  return request({
    url: '/system/dict/data/type/' + dictType,
    method: 'get'
  })
}

// 根据字典类型查询字典数据信息
export function getAllDicts (dictType) {
  return request({
    url: '/system/dict/data/all/type/' + dictType,
    method: 'get'
  })
}

// 新增字典数据
export function saveData (data) {
  return request({
    url: '/system/dict/data',
    method: 'post',
    data: data
  })
}

// 删除字典数据
export function delData (dictCode) {
  return request({
    url: '/system/dict/data/' + dictCode,
    method: 'delete'
  })
}

// 导出字典数据
export function exportData (query) {
  return request({
    url: '/system/dict/data/export',
    method: 'get',
    params: query
  })
}

// 查询字典类型列表
export function checkDictDataValueUnique (data) {
  return request({
    url: 'system/dict/data/checkDictDataValueUnique',
    method: 'get',
    params: data
  })
}

// 查询最大排序
export function findMaxSort (dictType) {
  return request({
    url: '/system/dict/data/findMaxSort/' + dictType,
    method: 'get'
  })
}
