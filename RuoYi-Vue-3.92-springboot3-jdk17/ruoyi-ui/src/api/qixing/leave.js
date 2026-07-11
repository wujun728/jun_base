import request from '@/utils/request'

// 查询员工请假列表
export function listLeave(query) {
  return request({
    url: '/system/leave/list',
    method: 'get',
    params: query
  })
}

// 查询员工请假详细
export function getLeave(id) {
  return request({
    url: '/system/leave/' + id,
    method: 'get'
  })
}

// 新增员工请假
export function addLeave(data) {
  return request({
    url: '/system/leave',
    method: 'post',
    data: data
  })
}

// 修改员工请假
export function updateLeave(data) {
  return request({
    url: '/system/leave',
    method: 'put',
    data: data
  })
}

// 删除员工请假
export function delLeave(id) {
  return request({
    url: '/system/leave/' + id,
    method: 'delete'
  })
}

// 鏌ヨLeave涓嬫媺鍒楄〃
export function listLeaveSelect(query) {
  return request({
    url: '/system/leave/listBySelect',
    method: 'get',
    params: query
  })
}