import request from '@/utils/request'

// 查询项目底稿列表
export function listDraft(query) {
  return request({
    url: '/system/draft/list',
    method: 'get',
    params: query
  })
}

// 查询项目底稿详细
export function getDraft(id) {
  return request({
    url: '/system/draft/' + id,
    method: 'get'
  })
}

// 新增项目底稿
export function addDraft(data) {
  return request({
    url: '/system/draft',
    method: 'post',
    data: data
  })
}

// 修改项目底稿
export function updateDraft(data) {
  return request({
    url: '/system/draft',
    method: 'put',
    data: data
  })
}

// 删除项目底稿
export function delDraft(id) {
  return request({
    url: '/system/draft/' + id,
    method: 'delete'
  })
}

// 鏌ヨDraft涓嬫媺鍒楄〃
export function listDraftSelect(query) {
  return request({
    url: '/system/draft/listBySelect',
    method: 'get',
    params: query
  })
}