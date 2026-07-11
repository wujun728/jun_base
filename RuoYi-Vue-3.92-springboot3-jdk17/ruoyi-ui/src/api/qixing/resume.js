import request from '@/utils/request'

// 查询面试候选人列表
export function listResume(query) {
  return request({
    url: '/system/resume/list',
    method: 'get',
    params: query
  })
}

// 查询面试候选人详细
export function getResume(id) {
  return request({
    url: '/system/resume/' + id,
    method: 'get'
  })
}

// 新增面试候选人
export function addResume(data) {
  return request({
    url: '/system/resume',
    method: 'post',
    data: data
  })
}

// 修改面试候选人
export function updateResume(data) {
  return request({
    url: '/system/resume',
    method: 'put',
    data: data
  })
}

// 删除面试候选人
export function delResume(id) {
  return request({
    url: '/system/resume/' + id,
    method: 'delete'
  })
}

// 鏌ヨResume涓嬫媺鍒楄〃
export function listResumeSelect(query) {
  return request({
    url: '/system/resume/listBySelect',
    method: 'get',
    params: query
  })
}