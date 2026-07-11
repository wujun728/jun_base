import request from '@/utils/request'

export function listOaLawInfo(query) {
  return request({
    url: '/system/law/list',
    method: 'get',
    params: query
  })
}

export function getOaLawInfo(id) {
  return request({
    url: '/system/law/' + id,
    method: 'get'
  })
}

export function addOaLawInfo(data) {
  return request({
    url: '/system/law',
    method: 'post',
    data: data
  })
}

export function updateOaLawInfo(data) {
  return request({
    url: '/system/law',
    method: 'put',
    data: data
  })
}

export function delOaLawInfo(id) {
  return request({
    url: '/system/law/' + id,
    method: 'delete'
  })
}