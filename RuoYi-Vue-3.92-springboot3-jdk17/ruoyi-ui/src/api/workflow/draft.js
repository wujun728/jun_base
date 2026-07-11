import request from '@/utils/request'

// 查询我起草的流程表列表
export function listDraft(query) {
  return request({
    url: '/workflow/draft/list',
    method: 'get',
    params: query
  })
}

// 查询我起草的流程表详细
export function getDraft(id) {
  return request({
    url: '/workflow/draft/' + id,
    method: 'get'
  })
}

// 新增我起草的流程表
export function addDraft(data) {
  return request({
    url: '/workflow/draft',
    method: 'post',
    data: data
  })
}

// 修改我起草的流程表
export function updateDraft(data) {
  return request({
    url: '/workflow/draft',
    method: 'put',
    data: data
  })
}

// 删除我起草的流程表
export function delDraft(id) {
  return request({
    url: '/workflow/draft/' + id,
    method: 'delete'
  })
}

// 统计数量
export function statMyDraft() {
  return request({
    url: '/workflow/draft/count',
    method: 'get'
  })
}
