import request from '@/utils/request'

// 查询模版管理列表
export function listTemplate(query) {
  return request({
    url: '/modelMessage/template/list',
    method: 'get',
    params: query
  })
}

// 查询模版管理详细
export function getTemplate(templateId) {
  return request({
    url: '/modelMessage/template/' + templateId,
    method: 'get'
  })
}

// 新增模版管理
export function addTemplate(data) {
  return request({
    url: '/modelMessage/template',
    method: 'post',
    data: data
  })
}

// 修改模版管理
export function updateTemplate(data) {
  return request({
    url: '/modelMessage/template',
    method: 'put',
    data: data
  })
}

// 删除模版管理
export function delTemplate(templateId) {
  return request({
    url: '/modelMessage/template/' + templateId,
    method: 'delete'
  })
}

// 查询模版签名
export function selecTemplates() {
  return request({
    url: '/modelMessage/template/selecTemplates',
    method: 'get'
  })
}