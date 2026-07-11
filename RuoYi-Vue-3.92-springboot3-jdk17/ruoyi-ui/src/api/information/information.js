import request from '@/utils/request'

// 查询新闻资讯列表
export function listInformation(query) {
  return request({
    url: '/information/news/list',
    method: 'get',
    params: query
  })
}

// 获取已发布的资讯
export function listPubInformation(query) {
  return request({
    url: '/information/news/pub/list',
    method: 'get',
    params: query
  })
}

// 查询新闻资讯详细
export function getInformation(id) {
  return request({
    url: '/information/news/' + id,
    method: 'get'
  })
}

// 新增新闻资讯
export function addInformation(data) {
  return request({
    url: '/information/news',
    method: 'post',
    data: data
  })
}

// 修改新闻资讯
export function updateInformation(data) {
  return request({
    url: '/information/news',
    method: 'put',
    data: data
  })
}

// 删除新闻资讯
export function delInformation(id) {
  return request({
    url: '/information/news/' + id,
    method: 'delete'
  })
}

// 变更状态
export function changeStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/information/news/changeStatus',
    method: 'put',
    data: data
  })
}

// 置顶
export function toTop(data) {
  return request({
    url: '/information/news/toTop' ,
    method: 'put',
    data: data
  })
}

// 增加阅读数
export function addReadNum(id) {
  return request({
    url: '/information/news/addReadNum/' + id,
    method: 'put'
  })
}