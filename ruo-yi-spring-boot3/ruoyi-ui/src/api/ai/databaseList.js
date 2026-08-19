import request from '@/utils/request'

// 查询向量库列表列表
export function listDatabaseList(query) {
  return request({
    url: '/ai/databaseList/list',
    method: 'get',
    params: query
  })
}

// 查询向量库列表详细
export function getDatabaseList(id) {
  return request({
    url: '/ai/databaseList/' + id,
    method: 'get'
  })
}

// 新增向量库列表
export function addDatabaseList(data) {
  return request({
    url: '/ai/databaseList',
    method: 'post',
    data: data
  })
}

// 修改向量库列表
export function updateDatabaseList(data) {
  return request({
    url: '/ai/databaseList',
    method: 'put',
    data: data
  })
}

// 删除向量库列表
export function delDatabaseList(id) {
  return request({
    url: '/ai/databaseList/' + id,
    method: 'delete'
  })
}
