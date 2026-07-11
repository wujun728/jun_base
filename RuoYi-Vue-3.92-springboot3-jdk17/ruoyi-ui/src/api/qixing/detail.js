import request from '@/utils/request'

// 查询考核模板明细列表
export function listDetail(query) {
  return request({
    url: '/system/detail/list',
    method: 'get',
    params: query
  })
}

// 查询考核模板明细详细
export function getDetail(id) {
  return request({
    url: '/system/detail/' + id,
    method: 'get'
  })
}

// 新增考核模板明细
export function addDetail(data) {
  return request({
    url: '/system/detail',
    method: 'post',
    data: data
  })
}

// 修改考核模板明细
export function updateDetail(data) {
  return request({
    url: '/system/detail',
    method: 'put',
    data: data
  })
}

// 删除考核模板明细
export function delDetail(id) {
  return request({
    url: '/system/detail/' + id,
    method: 'delete'
  })
}

// 鏌ヨDetail涓嬫媺鍒楄〃
export function listDetailSelect(query) {
  return request({
    url: '/system/detail/listBySelect',
    method: 'get',
    params: query
  })
}