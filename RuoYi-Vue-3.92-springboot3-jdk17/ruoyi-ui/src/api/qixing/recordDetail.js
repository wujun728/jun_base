import request from '@/utils/request'

export function listRecordDetail(query) {
  return request({
    url: '/system/recordDetail/list',
    method: 'get',
    params: query
  })
}

export function getRecordDetail(id) {
  return request({
    url: '/system/recordDetail/' + id,
    method: 'get'
  })
}

export function addRecordDetail(data) {
  return request({
    url: '/system/recordDetail',
    method: 'post',
    data: data
  })
}

export function updateRecordDetail(data) {
  return request({
    url: '/system/recordDetail',
    method: 'put',
    data: data
  })
}

export function delRecordDetail(id) {
  return request({
    url: '/system/recordDetail/' + id,
    method: 'delete'
  })
}