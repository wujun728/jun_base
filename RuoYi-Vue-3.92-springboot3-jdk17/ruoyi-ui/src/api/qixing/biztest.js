import request from '@/utils/request'

export function listBizTest(query) {
  return request({
    url: '/system/test/list',
    method: 'get',
    params: query
  })
}

export function getBizTest(id) {
  return request({
    url: '/system/test/' + id,
    method: 'get'
  })
}

export function addBizTest(data) {
  return request({
    url: '/system/test',
    method: 'post',
    data: data
  })
}

export function updateBizTest(data) {
  return request({
    url: '/system/test',
    method: 'put',
    data: data
  })
}

export function delBizTest(id) {
  return request({
    url: '/system/test/' + id,
    method: 'delete'
  })
}

export function listBizTestSelect(query) {
  return request({
    url: '/system/test/listBySelect',
    method: 'get',
    params: query
  })
}