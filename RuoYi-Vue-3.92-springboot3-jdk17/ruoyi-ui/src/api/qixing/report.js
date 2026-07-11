import request from '@/utils/request'

// 查询项目报告列表
export function listReport(query) {
  return request({
    url: '/system/report/list',
    method: 'get',
    params: query
  })
}

// 查询项目报告详细
export function getReport(id) {
  return request({
    url: '/system/report/' + id,
    method: 'get'
  })
}

// 新增项目报告
export function addReport(data) {
  return request({
    url: '/system/report',
    method: 'post',
    data: data
  })
}

// 修改项目报告
export function updateReport(data) {
  return request({
    url: '/system/report',
    method: 'put',
    data: data
  })
}

// 删除项目报告
export function delReport(id) {
  return request({
    url: '/system/report/' + id,
    method: 'delete'
  })
}

// 鏌ヨReport涓嬫媺鍒楄〃
export function listReportSelect(query) {
  return request({
    url: '/system/report/listBySelect',
    method: 'get',
    params: query
  })
}