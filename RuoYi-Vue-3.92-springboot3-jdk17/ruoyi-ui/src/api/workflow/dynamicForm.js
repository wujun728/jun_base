import request from '@/utils/request'

// 查询动态表单列表
export function listDynamicForm(query) {
  return request({
    url: '/template/dynamic/form/list',
    method: 'get',
    params: query
  })
}

// 查询动态表单详细
export function getDynamicForm(formId) {
  return request({
    url: '/template/dynamic/form/' + formId,
    method: 'get'
  })
}

// 新增动态表单
export function addDynamicForm(data) {
  return request({
    url: '/template/dynamic/form',
    method: 'post',
    data: data
  })
}

// 修改动态表单
export function updateDynamicForm(data) {
  return request({
    url: '/template/dynamic/form',
    method: 'put',
    data: data
  })
}

// 删除动态表单
export function delDynamicForm(formId) {
  return request({
    url: '/template/dynamic/form/' + formId,
    method: 'delete'
  })
}

// 查询可关联动态表单定义列表
export function getDynaFormOptionSelect() { 
  return request({
    url: '/template/dynamic/form/optionSelect',
    method: 'get',
  })
}
