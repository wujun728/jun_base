import request from '@/utils/request'

// 查询考核记录列表
export function listRecord(query) {
  return request({
    url: '/system/record/list',
    method: 'get',
    params: query
  })
}

// 查询考核记录详细
export function getRecord(id) {
  return request({
    url: '/system/record/' + id,
    method: 'get'
  })
}

// 新增考核记录
export function addRecord(data) {
  return request({
    url: '/system/record',
    method: 'post',
    data: data
  })
}

// 修改考核记录
export function updateRecord(data) {
  return request({
    url: '/system/record',
    method: 'put',
    data: data
  })
}

// 删除考核记录
export function delRecord(id) {
  return request({
    url: '/system/record/' + id,
    method: 'delete'
  })
}

// 鏌ヨRecord涓嬫媺鍒楄〃
export function listRecordSelect(query) {
  return request({
    url: '/system/record/listBySelect',
    method: 'get',
    params: query
  })
}