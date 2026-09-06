import request from '@/utils/request'

// 查询单数据列表
export function listData(query) {
  return request({
    url: '/form/data/list',
    method: 'get',
    params: query
  })
}

// 查询单数据详细
export function getData(dataId) {
  return request({
    url: '/form/data/' + dataId,
    method: 'get'
  })
}

// 新增单数据
export function addData(data) {
  return request({
    url: '/form/data',
    method: 'post',
    data: data
  })
}

// 修改单数据
export function updateData(data) {
  return request({
    url: '/form/data',
    method: 'put',
    data: data
  })
}

// 删除单数据
export function delData(dataId) {
  return request({
    url: '/form/data/' + dataId,
    method: 'delete'
  })
}
