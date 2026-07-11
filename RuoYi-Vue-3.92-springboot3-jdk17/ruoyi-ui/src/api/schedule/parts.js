import request from '@/utils/request'

// 查询日程参与人列表
export function listParts(query) {
  return request({
    url: '/schedule/parts/list',
    method: 'get',
    params: query
  })
}

// 查询日程参与人详细
export function getParts(id) {
  return request({
    url: '/schedule/parts/' + id,
    method: 'get'
  })
}

// 新增日程参与人
export function addParts(data) {
  return request({
    url: '/schedule/parts',
    method: 'post',
    data: data
  })
}

// 修改日程参与人
export function updateParts(data) {
  return request({
    url: '/schedule/parts',
    method: 'put',
    data: data
  })
}

// 删除日程参与人
export function delParts(id) {
  return request({
    url: '/schedule/parts/' + id,
    method: 'delete'
  })
}
