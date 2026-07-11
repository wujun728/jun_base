import request from '@/utils/request'

// 查询项目日报周报列表
export function listDaily(query) {
  return request({
    url: '/system/daily/list',
    method: 'get',
    params: query
  })
}

// 查询项目日报周报详细
export function getDaily(id) {
  return request({
    url: '/system/daily/' + id,
    method: 'get'
  })
}

// 新增项目日报周报
export function addDaily(data) {
  return request({
    url: '/system/daily',
    method: 'post',
    data: data
  })
}

// 修改项目日报周报
export function updateDaily(data) {
  return request({
    url: '/system/daily',
    method: 'put',
    data: data
  })
}

// 删除项目日报周报
export function delDaily(id) {
  return request({
    url: '/system/daily/' + id,
    method: 'delete'
  })
}

// 鏌ヨDaily涓嬫媺鍒楄〃
export function listDailySelect(query) {
  return request({
    url: '/system/daily/listBySelect',
    method: 'get',
    params: query
  })
}