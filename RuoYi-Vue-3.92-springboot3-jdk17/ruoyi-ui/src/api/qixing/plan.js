import request from '@/utils/request'

// 查询项目计划列表
export function listPlan(query) {
  return request({
    url: '/system/plan/list',
    method: 'get',
    params: query
  })
}

// 查询项目计划详细
export function getPlan(id) {
  return request({
    url: '/system/plan/' + id,
    method: 'get'
  })
}

// 新增项目计划
export function addPlan(data) {
  return request({
    url: '/system/plan',
    method: 'post',
    data: data
  })
}

// 修改项目计划
export function updatePlan(data) {
  return request({
    url: '/system/plan',
    method: 'put',
    data: data
  })
}

// 删除项目计划
export function delPlan(id) {
  return request({
    url: '/system/plan/' + id,
    method: 'delete'
  })
}

// 鏌ヨPlan涓嬫媺鍒楄〃
export function listPlanSelect(query) {
  return request({
    url: '/system/plan/listBySelect',
    method: 'get',
    params: query
  })
}