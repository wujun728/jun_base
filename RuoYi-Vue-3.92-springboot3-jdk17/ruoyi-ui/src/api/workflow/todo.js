import request from '@/utils/request'

// 查询待办列表
export function listTodoTable(query) {
  return request({
    url: '/workflow/todo/list/table',
    method: 'get',
    params: query
  })
}

// 查询待办折叠
export function listTodoCollapse(query) {
  return request({
    url: '/workflow/todo/list/collapse',
    method: 'get',
    params: query
  })
}

// 查询待办详细
export function getTodo(id) {
  return request({
    url: '/workflow/todo/' + id,
    method: 'get'
  })
}

// 新增待办
export function addTodo(data) {
  return request({
    url: '/workflow/todo',
    method: 'post',
    data: data
  })
}

// 修改待办
export function updateTodo(data) {
  return request({
    url: '/workflow/todo',
    method: 'put',
    data: data
  })
}

// 删除待办
export function delTodo(id) {
  return request({
    url: '/workflow/todo/' + id,
    method: 'delete'
  })
}

// 设置已读
export function readTodo(id) {
  return request({
    url: '/workflow/todo/readTodo/' + id,
    method: 'get'
  })
}

// 获取未读数
export function noRead() {
  return request({
    url: '/workflow/todo/noRead',
    method: 'get'
  })
}

// 阅办
export function readCopyTodo(id) {
  return request({
    url: '/workflow/handle/copy/read/' + id,
    method: 'get'
  })
}

// 统计待办数量
export function stat(type) {
  return request({
    url: '/workflow/todo/count/' + type,
    method: 'get'
  })
}
