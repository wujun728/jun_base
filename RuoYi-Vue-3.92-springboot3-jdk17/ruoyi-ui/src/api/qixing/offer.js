import request from '@/utils/request'

// 查询Offer发放列表
export function listOffer(query) {
  return request({
    url: '/system/offer/list',
    method: 'get',
    params: query
  })
}

// 查询Offer发放详细
export function getOffer(id) {
  return request({
    url: '/system/offer/' + id,
    method: 'get'
  })
}

// 新增Offer发放
export function addOffer(data) {
  return request({
    url: '/system/offer',
    method: 'post',
    data: data
  })
}

// 修改Offer发放
export function updateOffer(data) {
  return request({
    url: '/system/offer',
    method: 'put',
    data: data
  })
}

// 删除Offer发放
export function delOffer(id) {
  return request({
    url: '/system/offer/' + id,
    method: 'delete'
  })
}

// 鏌ヨOffer涓嬫媺鍒楄〃
export function listOfferSelect(query) {
  return request({
    url: '/system/offer/listBySelect',
    method: 'get',
    params: query
  })
}