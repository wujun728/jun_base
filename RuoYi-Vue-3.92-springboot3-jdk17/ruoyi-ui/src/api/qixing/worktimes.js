import request from '@/utils/request'

export function listWorktimes(query) {
  return request({
    url: '/system/worktimes/list',
    method: 'get',
    params: query
  })
}

export function getWorktimes(id) {
  return request({
    url: '/system/worktimes/' + id,
    method: 'get'
  })
}

export function addWorktimes(data) {
  return request({
    url: '/system/worktimes',
    method: 'post',
    data: data
  })
}

export function updateWorktimes(data) {
  return request({
    url: '/system/worktimes',
    method: 'put',
    data: data
  })
}

export function delWorktimes(id) {
  return request({
    url: '/system/worktimes/' + id,
    method: 'delete'
  })
}

export function listWorktimesSelect(query) {
  return request({
    url: '/system/worktimes/listBySelect',
    method: 'get',
    params: query
  })
}