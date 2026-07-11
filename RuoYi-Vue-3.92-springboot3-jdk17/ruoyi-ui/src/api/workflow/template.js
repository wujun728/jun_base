import request from '@/utils/request'

// 查询模板配置列表
export function listTemplate(query) {
  return request({
    url: '/template/template/list',
    method: 'get',
    params: query
  })
}

// 查询模板配置详细
export function getTemplate(id) {
  return request({
    url: '/template/template/' + id,
    method: 'get'
  })
}

// 新增模板配置
export function addTemplate(data) {
  return request({
    url: '/template/template',
    method: 'post',
    data: data
  })
}

// 修改模板配置
export function updateTemplate(data) {
  return request({
    url: '/template/template',
    method: 'put',
    data: data
  })
}

// 删除模板配置
export function delTemplate(id) {
  return request({
    url: '/template/template/' + id,
    method: 'delete'
  })
}

// 修改模板配置状态
export function changeEnable(data) {
  return request({
    url: '/template/template/changeEnableFlag',
    method: 'put',
    data: data
  })
}

// 查询新启流程模板列表
export function getNewStartTemplateList() {
  return request({
    url: '/template/template/newStart',
    method: 'get'
  })
}

// 查询新启流程模板列表
export function getSelectTemplateList() {
  return request({
    url: '/template/template/select',
    method: 'get'
  })
}

// 查询模板分类列表
export function listType(query) {
  return request({
    url: '/template/type/list',
    method: 'get',
    params: query
  })
}

// 查询有效的模板分类
export function listEnable() {
  return request({
    url: '/template/type/listEnable',
    method: 'get',
  })
}

// 查询模板分类详细
export function getType(id) {
  return request({
    url: '/template/type/' + id,
    method: 'get'
  })
}

// 新增模板分类
export function addType(data) {
  return request({
    url: '/template/type',
    method: 'post',
    data: data
  })
}

// 修改模板分类
export function updateType(data) {
  return request({
    url: '/template/type',
    method: 'put',
    data: data
  })
}

// 删除模板分类
export function delType(id) {
  return request({
    url: '/template/type/' + id,
    method: 'delete'
  })
}

// 更新启用状态
export function updateEnable(data) {
  return request({
    url: '/template/type/changeEnableFlag',
    method: 'put',
    data: data
  })
}