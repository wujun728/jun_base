import request from '@/utils/request'

// 查询mybatis在线接口列表
export function listMb(query) {
  return request({
    url: '/online/mb/list',
    method: 'get',
    params: query
  })
}

// 查询mybatis在线接口详细
export function getMb(mbId) {
  return request({
    url: '/online/mb/' + mbId,
    method: 'get'
  })
}

// 新增mybatis在线接口
export function addMb(data) {
  return request({
    url: '/online/mb',
    method: 'post',
    data: data
  })
}

// 修改mybatis在线接口
export function updateMb(data) {
  return request({
    url: '/online/mb',
    method: 'put',
    data: data
  })
}

// 删除mybatis在线接口
export function delMb(mbId) {
  return request({
    url: '/online/mb/' + mbId,
    method: 'delete'
  })
}
