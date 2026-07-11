import request from '@/utils/request'

// 查询面试汇总列表
export function listInterview(query) {
  return request({
    url: '/system/interview/list',
    method: 'get',
    params: query
  })
}

// 查询面试汇总详细
export function getInterview(id) {
  return request({
    url: '/system/interview/' + id,
    method: 'get'
  })
}

// 新增面试汇总
export function addInterview(data) {
  return request({
    url: '/system/interview',
    method: 'post',
    data: data
  })
}

// 修改面试汇总
export function updateInterview(data) {
  return request({
    url: '/system/interview',
    method: 'put',
    data: data
  })
}

// 删除面试汇总
export function delInterview(id) {
  return request({
    url: '/system/interview/' + id,
    method: 'delete'
  })
}

// 鏌ヨInterview涓嬫媺鍒楄〃
export function listInterviewSelect(query) {
  return request({
    url: '/system/interview/listBySelect',
    method: 'get',
    params: query
  })
}