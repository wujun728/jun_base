import request from '@/utils/request'

// 查询项目复核列表
export function listRecheck(query) {
  return request({
    url: '/system/recheck/list',
    method: 'get',
    params: query
  })
}

// 查询项目复核详细
export function getRecheck(id) {
  return request({
    url: '/system/recheck/' + id,
    method: 'get'
  })
}

// 新增项目复核
export function addRecheck(data) {
  return request({
    url: '/system/recheck',
    method: 'post',
    data: data
  })
}

// 修改项目复核
export function updateRecheck(data) {
  return request({
    url: '/system/recheck',
    method: 'put',
    data: data
  })
}

// 删除项目复核
export function delRecheck(id) {
  return request({
    url: '/system/recheck/' + id,
    method: 'delete'
  })
}

// 鏌ヨRecheck涓嬫媺鍒楄〃
export function listRecheckSelect(query) {
  return request({
    url: '/system/recheck/listBySelect',
    method: 'get',
    params: query
  })
}