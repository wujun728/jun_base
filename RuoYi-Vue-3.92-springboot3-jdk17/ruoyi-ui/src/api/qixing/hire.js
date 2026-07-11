import request from '@/utils/request'

// 查询录用审批列表
export function listHire(query) {
  return request({
    url: '/system/hire/list',
    method: 'get',
    params: query
  })
}

// 查询录用审批详细
export function getHire(id) {
  return request({
    url: '/system/hire/' + id,
    method: 'get'
  })
}

// 新增录用审批
export function addHire(data) {
  return request({
    url: '/system/hire',
    method: 'post',
    data: data
  })
}

// 修改录用审批
export function updateHire(data) {
  return request({
    url: '/system/hire',
    method: 'put',
    data: data
  })
}

// 删除录用审批
export function delHire(id) {
  return request({
    url: '/system/hire/' + id,
    method: 'delete'
  })
}

// 鏌ヨHire涓嬫媺鍒楄〃
export function listHireSelect(query) {
  return request({
    url: '/system/hire/listBySelect',
    method: 'get',
    params: query
  })
}