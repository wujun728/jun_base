import request from '@/utils/request'

// 查询回收站列表
export function listRecycle(query) {
  return request({
    url: '/workflow/recycle/list',
    method: 'get',
    params: query
  })
}

// 查询回收站详细
export function getRecycle(id) {
  return request({
    url: '/workflow/recycle/' + id,
    method: 'get'
  })
}

// 新增回收站
export function addRecycle(data) {
  return request({
    url: '/workflow/recycle',
    method: 'post',
    data: data
  })
}

// 修改回收站
export function updateRecycle(data) {
  return request({
    url: '/workflow/recycle',
    method: 'put',
    data: data
  })
}

// 删除回收站
export function delRecycle(id) {
  return request({
    url: '/workflow/recycle/' + id,
    method: 'delete'
  })
}
