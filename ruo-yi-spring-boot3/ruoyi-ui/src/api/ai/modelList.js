import request from '@/utils/request'

// 查询模型列表列表
export function listModelList(query) {
  return request({
    url: '/ai/modelList/list',
    method: 'get',
    params: query
  })
}

// 查询模型列表详细
export function getModelList(id) {
  return request({
    url: '/ai/modelList/' + id,
    method: 'get'
  })
}

// 新增模型列表
export function addModelList(data) {
  return request({
    url: '/ai/modelList',
    method: 'post',
    data: data
  })
}

// 修改模型列表
export function updateModelList(data) {
  return request({
    url: '/ai/modelList',
    method: 'put',
    data: data
  })
}

// 删除模型列表
export function delModelList(id) {
  return request({
    url: '/ai/modelList/' + id,
    method: 'delete'
  })
}
