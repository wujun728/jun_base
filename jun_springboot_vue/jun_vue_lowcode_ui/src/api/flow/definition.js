import request from '@/utils/request'

// 查询modeler列表
export function listDefinition(query) {
  return request({
    url: '/nocode/processDefinition/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getDefinitionsByInstanceId(instanceId) {
  return request({
    url: '/nocode/processDefinition/getDefinitions/' + instanceId,
    method: 'get'
  })
}

// 挂起激活转换
export function suspendOrActiveApply(data) {
  return request({
    url: '/nocode/processDefinition/suspendOrActiveApply',
    method: 'post',
    data:data
  })
}


// 删除Modeler
export function delDefinition(id) {
  return request({
    url: '/nocode/processDefinition/remove/' + id,
    method: 'delete'
  })
}


