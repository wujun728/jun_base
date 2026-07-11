import request from '@/utils/request'

// 查询办公用品申领申购列表
export function listCount(query) {
  return request({
    url: '/system/count/list',
    method: 'get',
    params: query
  })
}

// 查询办公用品申领申购详细
export function getCount(id) {
  return request({
    url: '/system/count/' + id,
    method: 'get'
  })
}

// 新增办公用品申领申购
export function addCount(data) {
  return request({
    url: '/system/count',
    method: 'post',
    data: data
  })
}

// 修改办公用品申领申购
export function updateCount(data) {
  return request({
    url: '/system/count',
    method: 'put',
    data: data
  })
}

// 删除办公用品申领申购
export function delCount(id) {
  return request({
    url: '/system/count/' + id,
    method: 'delete'
  })
}

// 鏌ヨCount涓嬫媺鍒楄〃
export function listCountSelect(query) {
  return request({
    url: '/system/count/listBySelect',
    method: 'get',
    params: query
  })
}