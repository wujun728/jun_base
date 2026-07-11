import request from '@/utils/request'

export function listTemplateDetail(query) {
  return request({
    url: '/system/templateDetail/list',
    method: 'get',
    params: query
  })
}

export function getTemplateDetail(id) {
  return request({
    url: '/system/templateDetail/' + id,
    method: 'get'
  })
}

export function addTemplateDetail(data) {
  return request({
    url: '/system/templateDetail',
    method: 'post',
    data: data
  })
}

export function updateTemplateDetail(data) {
  return request({
    url: '/system/templateDetail',
    method: 'put',
    data: data
  })
}

export function delTemplateDetail(id) {
  return request({
    url: '/system/templateDetail/' + id,
    method: 'delete'
  })
}