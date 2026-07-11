import request from '@/utils/request'

// 查询项目总结及评价列表
export function listAppraise(query) {
  return request({
    url: '/system/appraise/list',
    method: 'get',
    params: query
  })
}

// 查询项目总结及评价详细
export function getAppraise(id) {
  return request({
    url: '/system/appraise/' + id,
    method: 'get'
  })
}

// 新增项目总结及评价
export function addAppraise(data) {
  return request({
    url: '/system/appraise',
    method: 'post',
    data: data
  })
}

// 修改项目总结及评价
export function updateAppraise(data) {
  return request({
    url: '/system/appraise',
    method: 'put',
    data: data
  })
}

// 删除项目总结及评价
export function delAppraise(id) {
  return request({
    url: '/system/appraise/' + id,
    method: 'delete'
  })
}

// 鏌ヨAppraise涓嬫媺鍒楄〃
export function listAppraiseSelect(query) {
  return request({
    url: '/system/appraise/listBySelect',
    method: 'get',
    params: query
  })
}