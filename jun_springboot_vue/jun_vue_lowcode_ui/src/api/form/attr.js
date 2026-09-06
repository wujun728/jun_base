import request from '@/utils/request'

// 查询表单属性列表
export function listAttr(query) {
  return request({
    url: '/nocode/attr/list',
    method: 'get',
    params: query
  })
}

// 根据表单id查询应显示的表单属性列表（不分页）
export function listShowAttrByFormId(formId) {
  return request({
    url: '/nocode/attr/show/' + formId,
    method: 'get',
  })
}

// 查询表单属性详细
export function getAttr(id) {
  return request({
    url: '/nocode/attr/' + id,
    method: 'get'
  })
}

// 新增表单属性
export function addAttr(data) {
  return request({
    url: '/nocode/attr',
    method: 'post',
    data: data
  })
}

// 修改表单属性
export function updateAttr(data) {
  return request({
    url: '/nocode/attr',
    method: 'put',
    data: data
  })
}

// 删除表单属性
export function delAttr(id) {
  return request({
    url: '/nocode/attr/' + id,
    method: 'delete'
  })
}
