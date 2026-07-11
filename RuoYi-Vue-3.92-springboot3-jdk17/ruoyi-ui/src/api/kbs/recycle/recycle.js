import request from '@/utils/request'

// 查询知识库文档回收站列表
export function listRecycle(query) {
  return request({
    url: '/recycle/recycle/list',
    method: 'get',
    params: query
  })
}

// 查询知识库文档回收站详细
export function getRecycle(id) {
  return request({
    url: '/recycle/recycle/' + id,
    method: 'get'
  })
}

// 新增知识库文档回收站
export function addRecycle(data) {
  return request({
    url: '/recycle/recycle',
    method: 'post',
    data: data
  })
}

// 修改知识库文档回收站
export function updateRecycle(data) {
  return request({
    url: '/recycle/recycle',
    method: 'put',
    data: data
  })
}

// 删除知识库文档回收站
export function delRecycle(id) {
  return request({
    url: '/recycle/recycle/' + id,
    method: 'delete'
  })
}

// 修改知识库文档回收站
export function recoverRecycle(id) {
  return request({
    url: '/recycle/recycle/' + id,
    method: 'put',
  })
}
