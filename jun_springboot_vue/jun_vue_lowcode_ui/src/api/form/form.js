import request from '@/utils/request'

// 查询表单定义列表
export function listDef(query) {
  return request({
    url: '/nocode/form/list',
    method: 'get',
    params: query
  })
}

// 查询表单定义详细
export function getDef(id) {
  return request({
    url: '/nocode/form/' + id,
    method: 'get'
  })
}

// 新增表单定义
export function addDef(data) {
  return request({
    url: '/nocode/form',
    method: 'post',
    data: data
  })
}

// 修改表单定义
export function updateDef(data) {
  return request({
    url: '/nocode/form',
    method: 'put',
    data: data
  })
}

// 删除表单定义
export function delDef(id) {
  return request({
    url: '/nocode/form/' + id,
    method: 'delete'
  })
}

// 导出表单定义
export function exportDef(query) {
  return request({
    url: '/nocode/form/export',
    method: 'get',
    params: query
  })
}