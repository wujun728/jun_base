import request from '@/utils/request'

// 查询异步任务日志记录列表
export function listAsync(query) {
  return request({
    url: '/mq/async/list',
    method: 'get',
    params: query
  })
}

// 查询异步任务日志记录详细
export function getAsync(id) {
  return request({
    url: '/mq/async/' + id,
    method: 'get'
  })
}

// 重试异步任务日志记录
export function retry(id) {
  return request({
    url: '/mq/async/retry/'+ id,
    method: 'put'
  })
}

// 删除异步任务日志记录
export function delAsync(id) {
  return request({
    url: '/mq/async/' + id,
    method: 'delete'
  })
}
