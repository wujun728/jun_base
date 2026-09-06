import request from '@/utils/request'

// 查询单模板列表
export function listTemplate(query) {
  return request({
    url: '/form/template/list',
    method: 'get',
    params: query
  })
}

// 查询单模板详细
export function getTemplate(formId) {
  return request({
    url: '/form/template/' + formId,
    method: 'get'
  })
}

// 新增单模板
export function addTemplate(data) {
  return request({
    url: '/form/template',
    method: 'post',
    data: data
  })
}

// 修改单模板
export function updateTemplate(data) {
  return request({
    url: '/form/template',
    method: 'put',
    data: data
  })
}

// 删除单模板
export function delTemplate(formId) {
  return request({
    url: '/form/template/' + formId,
    method: 'delete'
  })
}
