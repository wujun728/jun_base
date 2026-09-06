/*
 * @Author: qianlishi
 * @Date: 2020-07-13 15:13:17
 * @Last Modified by: qianlishi
 * @Last Modified time: 2020-12-15 15:34:34
 */
import request from '@/utils/request'
import { setStorageItem } from '@/utils/storage'
// 数据字典和基础数据查询的相关接口

/**
 * 数据字典多类型编码查询接口
 * type参数 类型 String
 * type参数 格式 'type'
 */
export function getDictList (type) {
  return request({
    url: `/gaeaDict/select/${type}`,
    method: 'get',
  })
}
export function getDictCodes (project) {
  return request({
    url: `/gaeaDict/selectAll/${project}`,
    method: 'get',
  })
}

/**
 * 数据字典多类型编码查询接口
 * typeList参数 类型 Array
 * typeList参数 格式 ['type1','type2',...]
 */
export function getMultipleDictList (typeList) {
  const types = typeList + ''
  return request({
    url: `/v1/dict/types`,
    method: 'get',
    params: { types },
  })
}

/**
 * 主数据编码查询接口
 * typeList参数 类型 Array
 * typeList参数 格式 ['type1','type2',...]
 */
export function getBaseDataList (typeList) {
  const types = typeList + ''
  return request({
    url: `/v1/master/types`,
    method: 'get',
    params: { types },
  })
}

// 查询所有数据字典接口
export function getAllDict() {
  return request({
    url: '/gaeaDict/all',
    method: 'GET',
  })
}

// 将所有接口初始化到浏览器本地缓存
export function initDictToLocalstorage(callback) {
  getAllDict().then((res) => {
    if (res.code != 200) {
      console.error('初始化数据字典到local storage失败: ' + res.message)
      return
    }

    // 保存数据字典到localStorage
    setStorageItem('gaeaDict', res.data)
    if (callback != null) {
      callback()
    }
  })
}
