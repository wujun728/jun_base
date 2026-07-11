import request from '@/utils/request'

export function listBecomeMember(query) {
  return request({
    url: '/system/becomeMember/list',
    method: 'get',
    params: query
  })
}

export function getBecomeMember(id) {
  return request({
    url: '/system/becomeMember/' + id,
    method: 'get'
  })
}

export function addBecomeMember(data) {
  return request({
    url: '/system/becomeMember',
    method: 'post',
    data: data
  })
}

export function updateBecomeMember(data) {
  return request({
    url: '/system/becomeMember',
    method: 'put',
    data: data
  })
}

export function delBecomeMember(id) {
  return request({
    url: '/system/becomeMember/' + id,
    method: 'delete'
  })
}