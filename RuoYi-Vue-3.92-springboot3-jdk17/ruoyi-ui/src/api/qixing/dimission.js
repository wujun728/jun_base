import request from '@/utils/request'

// 查询离职列表
export function listDimission(query) {
  return request({
    url: '/system/dimission/list',
    method: 'get',
    params: query
  })
}

// 查询离职详细
export function getDimission(id) {
  return request({
    url: '/system/dimission/' + id,
    method: 'get'
  })
}

// 新增离职
export function addDimission(data) {
  return request({
    url: '/system/dimission',
    method: 'post',
    data: data
  })
}

// 修改离职
export function updateDimission(data) {
  return request({
    url: '/system/dimission',
    method: 'put',
    data: data
  })
}

// 删除离职
export function delDimission(id) {
  return request({
    url: '/system/dimission/' + id,
    method: 'delete'
  })
}

// 鏌ヨDimission涓嬫媺鍒楄〃
export function listDimissionSelect(query) {
  return request({
    url: '/system/dimission/listBySelect',
    method: 'get',
    params: query
  })
}