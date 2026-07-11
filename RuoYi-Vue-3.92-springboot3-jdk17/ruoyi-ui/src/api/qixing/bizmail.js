import request from '@/utils/request'

export function listBizMail(query) {
  return request({
    url: '/system/mail/list',
    method: 'get',
    params: query
  })
}

export function getBizMail(id) {
  return request({
    url: '/system/mail/' + id,
    method: 'get'
  })
}

export function addBizMail(data) {
  return request({
    url: '/system/mail',
    method: 'post',
    data: data
  })
}

export function updateBizMail(data) {
  return request({
    url: '/system/mail',
    method: 'put',
    data: data
  })
}

export function delBizMail(id) {
  return request({
    url: '/system/mail/' + id,
    method: 'delete'
  })
}

export function listBizMailSelect(query) {
  return request({
    url: '/system/mail/listBySelect',
    method: 'get',
    params: query
  })
}