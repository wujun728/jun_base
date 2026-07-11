import request from '@/utils/request'

// 查询入职报道列表
export function listReported(query) {
  return request({
    url: '/system/reported/list',
    method: 'get',
    params: query
  })
}

// 查询入职报道详细
export function getReported(id) {
  return request({
    url: '/system/reported/' + id,
    method: 'get'
  })
}

// 新增入职报道
export function addReported(data) {
  return request({
    url: '/system/reported',
    method: 'post',
    data: data
  })
}

// 修改入职报道
export function updateReported(data) {
  return request({
    url: '/system/reported',
    method: 'put',
    data: data
  })
}

// 删除入职报道
export function delReported(id) {
  return request({
    url: '/system/reported/' + id,
    method: 'delete'
  })
}

// 鏌ヨReported涓嬫媺鍒楄〃
export function listReportedSelect(query) {
  return request({
    url: '/system/reported/listBySelect',
    method: 'get',
    params: query
  })
}