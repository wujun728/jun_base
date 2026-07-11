import request from '@/utils/request'

// 查询项目借阅列表
export function listBorrow(query) {
  return request({
    url: '/system/borrow/list',
    method: 'get',
    params: query
  })
}

// 查询项目借阅详细
export function getBorrow(id) {
  return request({
    url: '/system/borrow/' + id,
    method: 'get'
  })
}

// 新增项目借阅
export function addBorrow(data) {
  return request({
    url: '/system/borrow',
    method: 'post',
    data: data
  })
}

// 修改项目借阅
export function updateBorrow(data) {
  return request({
    url: '/system/borrow',
    method: 'put',
    data: data
  })
}

// 删除项目借阅
export function delBorrow(id) {
  return request({
    url: '/system/borrow/' + id,
    method: 'delete'
  })
}

// 鏌ヨBorrow涓嬫媺鍒楄〃
export function listBorrowSelect(query) {
  return request({
    url: '/system/borrow/listBySelect',
    method: 'get',
    params: query
  })
}