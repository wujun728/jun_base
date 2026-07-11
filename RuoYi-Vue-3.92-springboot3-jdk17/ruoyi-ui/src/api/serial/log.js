import request from '@/utils/request'

// 查询编号生成日志列表
export function listLog(query) {
  return request({
    url: '/serial/log/list',
    method: 'get',
    params: query
  })
}

// 查询编号生成日志详细
export function getLog(id) {
  return request({
    url: '/serial/log/' + id,
    method: 'get'
  })
}

// 新增编号生成日志
export function addLog(data) {
  return request({
    url: '/serial/log',
    method: 'post',
    data: data
  })
}

// 修改编号生成日志
export function updateLog(data) {
  return request({
    url: '/serial/log',
    method: 'put',
    data: data
  })
}

// 删除编号生成日志
export function delLog(id) {
  return request({
    url: '/serial/log/' + id,
    method: 'delete'
  })
}
