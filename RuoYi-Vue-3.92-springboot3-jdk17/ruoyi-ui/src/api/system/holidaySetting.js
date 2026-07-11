import request from '@/utils/request'

// 查询假期设置列表
export function listSetting(query) {
  return request({
    url: '/system/holiday/setting/list',
    method: 'get',
    params: query
  })
}

// 获取指定月份的假日
export function listHoliday(query) {
  return request({
    url: '/system/holiday/setting/month/holiday',
    method: 'get',
    params: query
  })
}

// 获取指定月份的假日
export function listYearHoliday(year) {
  return request({
    url: '/system/holiday/setting/year/holiday/' + year,
    method: 'get'
  })
}

// 查询假期设置详细
export function getSetting(id) {
  return request({
    url: '/system/holiday/setting/' + id,
    method: 'get'
  })
}

// 新增假期设置
export function addSetting(data) {
  return request({
    url: '/system/holiday/setting',
    method: 'post',
    data: data
  })
}

// 修改假期设置
export function updateSetting(data) {
  return request({
    url: '/system/holiday/setting',
    method: 'put',
    data: data
  })
}

// 删除假期设置
export function delSetting(id) {
  return request({
    url: '/system/holiday/setting/' + id,
    method: 'delete'
  })
}
