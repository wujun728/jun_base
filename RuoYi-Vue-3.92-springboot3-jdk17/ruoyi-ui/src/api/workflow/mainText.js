import request from '@/utils/request'

// 查询正文详细
export function getMainInfo(param) {
  return request({
    url: '/workfile/main/getMainText',
    method: 'get',
    params: param
  })
}

// 上传正文
export function uploadMainText(data) {
  return request({
    url: '/workfile/main/uploadMainText',
    method: 'post',
    data: data
  })
}

// 删除正文
export function remove(business) {
  return request({
    url: '/workfile/main/remove/' + business,
    method: 'delete'
  })
}

// 正文盖章
export function stamp(data) {
  return request({
    url: '/workfile/main/stamp',
    method: 'post',
    data: data
  })
}

// 还原印章
export function restoreSeal(businessId) {
  return request({
    url: '/workfile/main/restoreSeal/' + businessId,
    method: 'put'
  })
}
