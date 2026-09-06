import request from '@/utils/request'

export function reportList(params) {
  return request({
    url: '/report/pageList',
    method: 'GET',
    params,
  })
}

export function reportAdd(data) {
  return request({
    url: '/report',
    method: 'post',
    data
  })
}

export function reportDeleteBatch(data) {
  return request({
    url: '/report/delete/batch',
    method: 'post',
    data
  })
}

export function reportUpdate(data) {
  return request({
    url: '/report',
    method: 'put', data,
  })
}

export function reportDetail(data) {
  return request({
    url: '/report/' + data.id,
    method: 'get',
    params: { accessKey: data.accessKey }
  })
}

export default { reportList, reportAdd, reportDeleteBatch, reportUpdate, reportDetail }
