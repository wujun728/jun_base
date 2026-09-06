import request from '@/utils/request'

// 查询逻辑演示列表
export function listLogicLogic(query) {
  return request({
    url: '/logic/LogicLogic/list',
    method: 'get',
    params: query
  })
}

// 查询逻辑演示列表不分页
export function listAllLogicLogic(query) {
  return request({
    url: '/logic/LogicLogic/listAll',
    method: 'get',
    params: query
  })
}

// 查询逻辑演示详细
export function getLogicLogic(id) {
  return request({
    url: '/logic/LogicLogic/' + id,
    method: 'get'
  })
}

// 新增逻辑演示
export function addLogicLogic(data) {
  return request({
    url: '/logic/LogicLogic',
    method: 'post',
    data: data
  })
}

// 修改逻辑演示
export function updateLogicLogic(data) {
  return request({
    url: '/logic/LogicLogic',
    method: 'put',
    data: data
  })
}

// 删除逻辑演示
export function delLogicLogic(id) {
  return request({
    url: '/logic/LogicLogic/' + id,
    method: 'delete'
  })
}
