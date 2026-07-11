import request from '@/utils/request'

// 查询知识库文档评论列表
export function listComment(query) {
  return request({
    url: '/document/comment/list',
    method: 'get',
    params: query
  })
}

// 查询知识库文档评论详细
export function getComment(id) {
  return request({
    url: '/document/comment/' + id,
    method: 'get'
  })
}

// 新增知识库文档评论
export function addComment(data) {
  return request({
    url: '/document/comment',
    method: 'post',
    data: data
  })
}

// 修改知识库文档评论
export function updateComment(data) {
  return request({
    url: '/document/comment',
    method: 'put',
    data: data
  })
}

// 删除知识库文档评论
export function delComment(id) {
  return request({
    url: '/document/comment/' + id,
    method: 'delete'
  })
}

// 查询知识库文档子评论列表
export function listCommentByParent(query) {
  return request({
    url: '/document/comment/listByParent',
    method: 'get',
    params: query
  })
}
