import request from '@/utils/request'

// 查询变量管理列表
export function listVariable(query) {
  return request({
    url: '/modelMessage/variable/list',
    method: 'get',
    params: query
  })
}

// 查询变量管理详细
export function getVariable(variableId) {
  return request({
    url: '/modelMessage/variable/' + variableId,
    method: 'get'
  })
}

// 新增变量管理
export function addVariable(data) {
  return request({
    url: '/modelMessage/variable',
    method: 'post',
    data: data
  })
}

// 修改变量管理
export function updateVariable(data) {
  return request({
    url: '/modelMessage/variable',
    method: 'put',
    data: data
  })
}

// 删除变量管理
export function delVariable(variableId) {
  return request({
    url: '/modelMessage/variable/' + variableId,
    method: 'delete'
  })
}

// 查询变量
export function selectVariable() {
  return request({
    url: '/modelMessage/variable/selectMessageVariable',
    method: 'get'
  })
}
