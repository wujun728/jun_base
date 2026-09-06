import request from '@/utils/hsj/service'
import { interceptorsMes, interceptorsData } from '../interceptors/index'
import { mainBase } from '../config'
/**
 * 根据查询条件获取数据
 *
 * @param {string} url 接口地址
 * @param {Object} params 查询条件
 * @returns 接口返回的数据
 */
export function getPageListData(url, params) {
  return request.get({
    url: mainBase + url,
    params,
    interceptors: interceptorsMes,
  })
}

/**
 * 删除数据
 *
 * @param {string} url 接口地址
 * @returns {Promise} 接口返回的数据
 */
export function deletData(url) {
  return request.delete({
    url: mainBase + url,
    interceptors: interceptorsMes,
  })
}

/**
 * 修改数据
 *
 * @param {string} url 接口地址
 * @param {Object} editData 要修改的对象
 * @returns 接口返回的数据
 */
export function editData(url, editData) {
  return request.put({
    url: mainBase + url,
    data: editData,
    interceptors: interceptorsMes,
  })
}
/**
 * 添加数据
 *
 * @param {string} url 接口地址
 * @param {Object} newData 要创建的数据
 * @returns 接口返回的数据
 */
export function createData(url, newData) {
  return request.post({
    url: mainBase + url,
    data: newData,
    interceptors: interceptorsMes,
  })
}

/**
 * 查询单条数据
 *
 * @param {string} url 接口地址
 * @returns 接口返回的数据
 */
export function getInfo(url) {
  return request.get({
    url: mainBase + url,
    interceptors: interceptorsData,
  })
}
