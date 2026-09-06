/**
 *
 *
 * @author liukx
 * @date  -
 */
import request from '@/utils/request'

// 查询服务注册列表
export function listServer(query) {
  return request({
    url: '/system/server/list',
    method: 'get',
    params: query
  })
}

// 查询服务注册详细
export function getServer(id) {
  return request({
    url: '/system/server/' + id,
    method: 'get'
  })
}

// 新增服务注册
export function addServer(data) {
  return request({
    url: '/system/server',
    method: 'post',
    data: data
  })
}

// 修改服务注册
export function updateServer(data) {
  return request({
    url: '/system/server',
    method: 'put',
    data: data
  })
}

// 删除服务注册
export function delServer(id) {
  return request({
    url: '/system/server/' + id,
    method: 'delete'
  })
}

// 导出服务注册
export function exportServer(query) {
  return request({
    url: '/system/server/export',
    method: 'get',
    params: query
  })
}
