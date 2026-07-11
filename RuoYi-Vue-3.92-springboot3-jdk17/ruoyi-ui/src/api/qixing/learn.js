import request from '@/utils/request'

export function listOaLearnInfo(query) {
  return request({
    url: '/system/learn/list',
    method: 'get',
    params: query
  })
}

export function getOaLearnInfo(id) {
  return request({
    url: '/system/learn/' + id,
    method: 'get'
  })
}

export function addOaLearnInfo(data) {
  return request({
    url: '/system/learn',
    method: 'post',
    data: data
  })
}

export function updateOaLearnInfo(data) {
  return request({
    url: '/system/learn',
    method: 'put',
    data: data
  })
}

export function delOaLearnInfo(id) {
  return request({
    url: '/system/learn/' + id,
    method: 'delete'
  })
}