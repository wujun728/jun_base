import request from '@/utils/request'

export function listOaNotesInfo(query) {
  return request({
    url: '/system/notes/list',
    method: 'get',
    params: query
  })
}

export function getOaNotesInfo(id) {
  return request({
    url: '/system/notes/' + id,
    method: 'get'
  })
}

export function addOaNotesInfo(data) {
  return request({
    url: '/system/notes',
    method: 'post',
    data: data
  })
}

export function updateOaNotesInfo(data) {
  return request({
    url: '/system/notes',
    method: 'put',
    data: data
  })
}

export function delOaNotesInfo(id) {
  return request({
    url: '/system/notes/' + id,
    method: 'delete'
  })
}