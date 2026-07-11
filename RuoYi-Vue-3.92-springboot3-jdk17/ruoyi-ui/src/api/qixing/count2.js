import request from '@/utils/request'

// 查询办公用品申领申购列表
export function listCount2(query) {
  return request({
    url: '/system/count2/list',
    method: 'get',
    params: query
  })
}

// 查询办公用品申领申购详细
export function getCount2(id) {
  return request({
    url: '/system/count2/' + id,
    method: 'get'
  })
}

// 新增办公用品申领申购
export function addCount2(data) {
  return request({
    url: '/system/count2',
    method: 'post',
    data: data
  })
}

// 修改办公用品申领申购
export function updateCount2(data) {
  return request({
    url: '/system/count2',
    method: 'put',
    data: data
  })
}

// 删除办公用品申领申购
export function delCount2(id) {
  return request({
    url: '/system/count2/' + id,
    method: 'delete'
  })
}

// 鏌ヨCount2涓嬫媺鍒楄〃
export function listCount2Select(query) {
  return request({
    url: '/system/count2/listBySelect',
    method: 'get',
    params: query
  })
}