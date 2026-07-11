import request from '@/utils/request'

// 查询知识库收藏列表
export function listFavorite(query) {
  return request({
    url: '/favorite/favorite/list',
    method: 'get',
    params: query
  })
}

// 查询知识库收藏详细
export function getFavorite(id) {
  return request({
    url: '/favorite/favorite/' + id,
    method: 'get'
  })
}

// 新增知识库收藏
export function addFavorite(data) {
  return request({
    url: '/favorite/favorite',
    method: 'post',
    data: data
  })
}

// 修改知识库收藏
export function updateFavorite(data) {
  return request({
    url: '/favorite/favorite',
    method: 'put',
    data: data
  })
}

// 删除知识库收藏
export function delFavorite(id) {
  return request({
    url: '/favorite/favorite/' + id,
    method: 'delete'
  })
}

// 统计文档收藏量
export function statFavorite(docId) {
  return request({
    url: '/favorite/favorite/stat/' + docId,
    method: 'get',
  })
}

// 查询知识库收藏
export function getFavoriteByUser(docId) {
  return request({
    url: '/favorite/favorite/infoByUserId/' + docId,
    method: 'get'
  })
}

// 取消知识库收藏
export function cancelFavorite(docId) {
  return request({
    url: '/favorite/favorite/cancel/' + docId,
    method: 'delete'
  })
}
