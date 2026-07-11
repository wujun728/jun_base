import request from '@/utils/request'

// 查询政策法规列表
export function listInfo(query) {
  return request({
    url: '/system/info/list',
    method: 'get',
    params: query
  })
}

// 查询政策法规详细
export function getInfo(id) {
  return request({
    url: '/system/info/' + id,
    method: 'get'
  })
}

// 新增政策法规
export function addInfo(data) {
  return request({
    url: '/system/info',
    method: 'post',
    data: data
  })
}

// 修改政策法规
export function updateInfo(data) {
  return request({
    url: '/system/info',
    method: 'put',
    data: data
  })
}

// 删除政策法规
export function delInfo(id) {
  return request({
    url: '/system/info/' + id,
    method: 'delete'
  })
}

// 鏌ヨInfo涓嬫媺鍒楄〃
export function listInfoSelect(query) {
  return request({
    url: '/system/info/listBySelect',
    method: 'get',
    params: query
  })
}