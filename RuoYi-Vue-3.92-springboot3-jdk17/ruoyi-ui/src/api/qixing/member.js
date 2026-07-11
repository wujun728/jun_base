import request from '@/utils/request'

// 查询转正列表
export function listMember(query) {
  return request({
    url: '/system/member/list',
    method: 'get',
    params: query
  })
}

// 查询转正详细
export function getMember(id) {
  return request({
    url: '/system/member/' + id,
    method: 'get'
  })
}

// 新增转正
export function addMember(data) {
  return request({
    url: '/system/member',
    method: 'post',
    data: data
  })
}

// 修改转正
export function updateMember(data) {
  return request({
    url: '/system/member',
    method: 'put',
    data: data
  })
}

// 删除转正
export function delMember(id) {
  return request({
    url: '/system/member/' + id,
    method: 'delete'
  })
}

// 鏌ヨMember涓嬫媺鍒楄〃
export function listMemberSelect(query) {
  return request({
    url: '/system/member/listBySelect',
    method: 'get',
    params: query
  })
}