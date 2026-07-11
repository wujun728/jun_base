import request from '@/utils/request'

// 查询项目信息列表
export function listProject(query) {
  return request({
    url: '/system/project/list',
    method: 'get',
    params: query
  })
}

// 查询项目信息详细
export function getProject(id) {
  return request({
    url: '/system/project/' + id,
    method: 'get'
  })
}

// 新增项目信息
export function addProject(data) {
  return request({
    url: '/system/project',
    method: 'post',
    data: data
  })
}

// 修改项目信息
export function updateProject(data) {
  return request({
    url: '/system/project',
    method: 'put',
    data: data
  })
}

// 删除项目信息
export function delProject(id) {
  return request({
    url: '/system/project/' + id,
    method: 'delete'
  })
}

// 鏌ヨProject涓嬫媺鍒楄〃
export function listProjectSelect(query) {
  return request({
    url: '/system/project/listBySelect',
    method: 'get',
    params: query
  })
}