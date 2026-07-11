import request from '@/utils/request'

// 查询已办列表
export function listDone(query) {
  return request({
    url: '/workflow/done/list',
    method: 'get',
    params: query
  })
}

// 查询已办详细
export function getDone(id) {
  return request({
    url: '/workflow/done/' + id,
    method: 'get'
  })
}

// 新增已办
export function addDone(data) {
  return request({
    url: '/workflow/done',
    method: 'post',
    data: data
  })
}

// 修改已办
export function updateDone(data) {
  return request({
    url: '/workflow/done',
    method: 'put',
    data: data
  })
}

// 删除已办
export function delDone(id) {
  return request({
    url: '/workflow/done/' + id,
    method: 'delete'
  })
}

// 催办
export function urgeAll(data) {
  return request({
    url: '/workflow/done/urge',
    method: 'post',
    data: data
  })
}