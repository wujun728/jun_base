import type { TableDataInfo } from '@/types/api/system/common'
import type { PostQueryParams, SysPost } from '@/types/api/system/post'

import response from '@/utils/http'

// 查询岗位列表
export function listPost(query: PostQueryParams) {
  return response<TableDataInfo<SysPost>>({
    url: '/system/post/list',
    method: 'get',
    params: query
  })
}

// 查询岗位详细
export function getPost(postId: number) {
  return response<SysPost>({
    url: '/system/post/' + postId,
    method: 'get'
  })
}

// 新增岗位
export function addPost(data: SysPost) {
  return response({
    url: '/system/post',
    method: 'post',
    data
  })
}

// 修改岗位
export function updatePost(data: SysPost) {
  return response({
    url: '/system/post',
    method: 'put',
    data
  })
}

// 删除岗位
export function delPost(postId: number | number[]) {
  return response({
    url: '/system/post/' + postId,
    method: 'delete'
  })
}
