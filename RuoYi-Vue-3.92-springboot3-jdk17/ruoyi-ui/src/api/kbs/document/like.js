import request from '@/utils/request'

// 新增知识库文档评论点赞
export function addLike(data) {
  return request({
    url: '/document/comment/like',
    method: 'post',
    data: data
  })
}

// 删除知识库文档评论点赞
export function delLike(commentId) {
  return request({
    url: '/document/comment/like/' + commentId,
    method: 'delete'
  })
}
