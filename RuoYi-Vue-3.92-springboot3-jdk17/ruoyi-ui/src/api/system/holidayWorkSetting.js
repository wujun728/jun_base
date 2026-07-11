import request from '@/utils/request'

// 查询节假日补班设置列表
export function listSetting(query) {
  return request({
    url: '/system/holiday/worksetting/list',
    method: 'get',
    params: query
  })
}

// 获取指定月份的补班情况
export function listWork(query) {
  return request({
    url: '/system/holiday/worksetting/month/work',
    method: 'get',
    params: query
  })
}

// 查询节假日补班设置详细
export function getSetting(id) {
  return request({
    url: '/system/holiday/worksetting/' + id,
    method: 'get'
  })
}

// 新增节假日补班设置
export function addSetting(data) {
  return request({
    url: '/system/holiday/worksetting',
    method: 'post',
    data: data
  })
}

// 修改节假日补班设置
export function updateSetting(data) {
  return request({
    url: '/system/holiday/worksetting',
    method: 'put',
    data: data
  })
}

// 删除节假日补班设置
export function delSetting(id) {
  return request({
    url: '/system/holiday/worksetting/' + id,
    method: 'delete'
  })
}
