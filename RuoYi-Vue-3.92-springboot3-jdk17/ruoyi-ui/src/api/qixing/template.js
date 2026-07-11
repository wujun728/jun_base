import request from '@/utils/request'

// 查询考核模板列表
export function listTemplate(query) {
  return request({
    url: '/system/template/list',
    method: 'get',
    params: query
  })
}

// 查询考核模板详细
export function getTemplate(id) {
  return request({
    url: '/system/template/' + id,
    method: 'get'
  })
}

// 新增考核模板
export function addTemplate(data) {
  return request({
    url: '/system/template',
    method: 'post',
    data: data
  })
}

// 修改考核模板
export function updateTemplate(data) {
  return request({
    url: '/system/template',
    method: 'put',
    data: data
  })
}

// 删除考核模板
export function delTemplate(id) {
  return request({
    url: '/system/template/' + id,
    method: 'delete'
  })
}

// 鏌ヨTemplate涓嬫媺鍒楄〃
export function listTemplateSelect(query) {
  return request({
    url: '/system/template/listBySelect',
    method: 'get',
    params: query
  })
}