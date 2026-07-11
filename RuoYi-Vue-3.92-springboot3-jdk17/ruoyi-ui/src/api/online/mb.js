import request from '@/utils/request'

export function listMb(query) {
  return request({
    url: '/online/mb/list',
    method: 'get',
    params: query
  })
}

export function getMb(id) {
  return request({
    url: '/online/mb/' + id,
    method: 'get'
  })
}

export function addMb(data) {
  return request({
    url: '/online/mb',
    method: 'post',
    data: data
  })
}

export function updateMb(data) {
  return request({
    url: '/online/mb',
    method: 'put',
    data: data
  })
}

export function delMb(id) {
  return request({
    url: '/online/mb/' + id,
    method: 'delete'
  })
}

export function exportMb(query) {
  return request({
    url: '/online/mb/export',
    method: 'get',
    params: query
  })
}
