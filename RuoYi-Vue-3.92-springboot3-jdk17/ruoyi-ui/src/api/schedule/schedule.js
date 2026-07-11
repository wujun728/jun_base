import request from '@/utils/request'

// 查询日程列表
export function listSchedule(query) {
  return request({
    url: '/schedule/schedule/list',
    method: 'get',
    params: query
  })
}

// 查询月度日程
export function listMonth(query) {
  return request({
    url: '/schedule/schedule/month',
    method: 'get',
    params: query
  })
}

// 获取当天日程
export function listDay(query) {
  return request({
    url: '/schedule/schedule/day',
    method: 'get',
    params: query
  })
}

// 获取包含日程的月度日期数据
export function listContainDate(query) {
  return request({
    url: '/schedule/schedule/contain/date',
    method: 'get',
    params: query
  })
}

// 查询日程详细
export function getSchedule(id) {
  return request({
    url: '/schedule/schedule/' + id,
    method: 'get'
  })
}

// 新增日程
export function saveSchedule(data) {
  return request({
    url: '/schedule/schedule',
    method: 'post',
    data: data
  })
}

// 删除日程
export function delSchedule(id) {
  return request({
    url: '/schedule/schedule/' + id,
    method: 'delete'
  })
}
