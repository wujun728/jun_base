import request from '@/utils/request'

// 查询项目报告文号列表
export function listReportnumber(query) {
  return request({
    url: '/system/reportnumber/list',
    method: 'get',
    params: query
  })
}

// 查询项目报告文号详细
export function getReportnumber(id) {
  return request({
    url: '/system/reportnumber/' + id,
    method: 'get'
  })
}

// 新增项目报告文号
export function addReportnumber(data) {
  return request({
    url: '/system/reportnumber',
    method: 'post',
    data: data
  })
}

// 修改项目报告文号
export function updateReportnumber(data) {
  return request({
    url: '/system/reportnumber',
    method: 'put',
    data: data
  })
}

// 删除项目报告文号
export function delReportnumber(id) {
  return request({
    url: '/system/reportnumber/' + id,
    method: 'delete'
  })
}

// 鏌ヨReportnumber涓嬫媺鍒楄〃
export function listReportnumberSelect(query) {
  return request({
    url: '/system/reportnumber/listBySelect',
    method: 'get',
    params: query
  })
}