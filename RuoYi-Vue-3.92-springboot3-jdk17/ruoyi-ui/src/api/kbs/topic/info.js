import request from '@/utils/request'

// 查询知识库主题列表
export function listInfo(query) {
  return request({
    url: '/topic/info/list',
    method: 'get',
    params: query
  })
}

// 获取主体列表，按类型分组
export function listByCategory(query) {
  return request({
    url: '/topic/info/listByCategory',
    method: 'get',
    params: query
  })
}

// 查询知识库主题详细
export function getTopicInfo(id) {
  return request({
    url: '/topic/info/' + id,
    method: 'get'
  })
}

// 新增知识库主题
export function addInfo(data) {
  return request({
    url: '/topic/info',
    method: 'post',
    data: data
  })
}

// 修改知识库主题
export function updateInfo(data) {
  return request({
    url: '/topic/info',
    method: 'put',
    data: data
  })
}

// 删除知识库主题
export function delInfo(id) {
  return request({
    url: '/topic/info/' + id,
    method: 'delete'
  })
}

// 获取文档树
export function docTree(query) {
  return request({
    url: '/document/base/listByTopic',
    method: 'get',
    params: query
  })
}

// 获取文档详情
export function docInfo(id) {
  return request({
    url: '/document/base/' + id,
    method: 'get'
  })
}

// 新增知识文档
export function addDoc(data) {
  return request({
    url: '/document/base',
    method: 'post',
    data: data
  })
}

// 修改文档
export function editDoc(data) {
  return request({
    url: '/document/base',
    method: 'put',
    data: data
  })
}

// 删除文档
export function delDoc(id) {
  return request({
    url: '/document/base/' + id,
    method: 'delete'
  })
}

// 重命名文档
export function resetName(data) {
  return request({
    url: '/document/base/resetName',
    method: 'put',
    data: data
  })
}

// 重排序
export function reSort(data) {
  return request({
    url: '/document/base/reSort',
    method: 'put',
    data: data
  })
}

// 查询知识库主题所有详细
export function getAllInfo(id) {
  return request({
    url: '/topic/info/allInfo/' + id,
    method: 'get'
  })
}