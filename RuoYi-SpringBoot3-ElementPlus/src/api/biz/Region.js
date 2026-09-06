import request from '@/utils/request'

// 查询中国行政区划（省市区县乡）列表
export function listRegion(query) {
  return request({
    url: '/biz/Region/list',
    method: 'get',
    params: query
  })
}

// 查询中国行政区划（省市区县乡）详细
export function getRegion(id) {
  return request({
    url: '/biz/Region/' + id,
    method: 'get'
  })
}

// 新增中国行政区划（省市区县乡）
export function addRegion(data) {
  return request({
    url: '/biz/Region',
    method: 'post',
    data: data
  })
}

// 修改中国行政区划（省市区县乡）
export function updateRegion(data) {
  return request({
    url: '/biz/Region',
    method: 'put',
    data: data
  })
}

// 删除中国行政区划（省市区县乡）
export function delRegion(id) {
  return request({
    url: '/biz/Region/' + id,
    method: 'delete'
  })
}
