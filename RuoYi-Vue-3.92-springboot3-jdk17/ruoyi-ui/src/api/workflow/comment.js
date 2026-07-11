import request from '@/utils/request'

// 查询流程常用意见列表
export function listComments(query) {
  return request({
    url: '/workflow/comment/list',
    method: 'get',
    params: query
  })
}

// 新增流程常用意见
export function addComment(data) {
  return request({
    url: '/workflow/comment',
    method: 'post',
    data: data
  })
}

// 修改流程常用意见
export function updateComment(data) {
  return request({
    url: '/workflow/comment',
    method: 'put',
    data: data
  })
}

// 删除流程常用意见
export function delComment(id) {
  return request({
    url: '/workflow/comment/' + id,
    method: 'delete'
  })
}
