import request from '@/utils/request'

// 查询项目进度与任务(WBS)列表
export function listTask(query) {
  return request({
    url: '/system/task/list',
    method: 'get',
    params: query
  })
}

// 查询项目进度与任务(WBS)详细
export function getTask(id) {
  return request({
    url: '/system/task/' + id,
    method: 'get'
  })
}

// 新增项目进度与任务(WBS)
export function addTask(data) {
  return request({
    url: '/system/task',
    method: 'post',
    data: data
  })
}

// 修改项目进度与任务(WBS)
export function updateTask(data) {
  return request({
    url: '/system/task',
    method: 'put',
    data: data
  })
}

// 删除项目进度与任务(WBS)
export function delTask(id) {
  return request({
    url: '/system/task/' + id,
    method: 'delete'
  })
}

// 鏌ヨTask涓嬫媺鍒楄〃
export function listTaskSelect(query) {
  return request({
    url: '/system/task/listBySelect',
    method: 'get',
    params: query
  })
}