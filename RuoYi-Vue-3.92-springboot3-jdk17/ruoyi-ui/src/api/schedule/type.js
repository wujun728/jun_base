import request from '@/utils/request'

// 查询日程分类列表
export function listType(query) {
  return request({
    url: '/schedule/type/list',
    method: 'get',
    params: query
  })
}

// 获取个人全部数据
export function listAll() {
  return request({
    url: '/schedule/type/person/all',
    method: 'get'
  })
}

// 查询日程分类详细
export function getType(id) {
  return request({
    url: '/schedule/type/' + id,
    method: 'get'
  })
}

// 新增日程分类
export function addType(data) {
  return request({
    url: '/schedule/type',
    method: 'post',
    data: data
  })
}

// 修改日程分类
export function updateType(data) {
  return request({
    url: '/schedule/type',
    method: 'put',
    data: data
  })
}

// 删除日程分类
export function delType(id) {
  return request({
    url: '/schedule/type/' + id,
    method: 'delete'
  })
}
