import request from '@/utils/request'

// 查询编号配置列表
export function listConfig(query) {
  return request({
    url: '/serial/config/list',
    method: 'get',
    params: query
  })
}

// 查询有效的编号配置
export function listAllOptions() {
  return request({
    url: '/serial/config/serialOptions',
    method: 'get'
  })
}

// 查询编号配置详细
export function getConfig(id) {
  return request({
    url: '/serial/config/' + id,
    method: 'get'
  })
}

// 新增编号配置
export function addConfig(data) {
  return request({
    url: '/serial/config',
    method: 'post',
    data: data
  })
}

// 修改编号配置
export function updateConfig(data) {
  return request({
    url: '/serial/config',
    method: 'put',
    data: data
  })
}

// 删除编号配置
export function delConfig(id) {
  return request({
    url: '/serial/config/' + id,
    method: 'delete'
  })
}

// 启、停配置
export function changeEnableFlag(id, enableFlag) {
  const data = {
    id,
    enableFlag
  }
  return request({
    url: '/serial/config/changeEnableFlag',
    method: 'put',
    data: data
  })
}

// 生成编号
export function genSerialNo(confId) {
  return request({
    url: '/serial/config/genSerialNo/' + confId,
    method: 'get'
  })
} 