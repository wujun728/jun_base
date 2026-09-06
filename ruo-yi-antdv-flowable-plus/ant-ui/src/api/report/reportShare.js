import request from '@/utils/request'

export function reportShareList(params) {
  return request({
    url: 'reportShare/pageList',
    method: 'GET',
    params,
  })
}

export function reportShareAdd(data) {
  return request({
    url: 'report/share',
    method: 'post',
    data
  })
}

export function reportShareDeleteBatch(data) {
return request({
url: 'reportShare/delete/batch',
method: 'post',
data
})
}

export function reportShareUpdate(data) {
  return request({
    url: 'reportShare',
    method: 'put', data,
  })
}

export function reportShareDetail(data) {
  return request({
    url: 'reportShare/' + data.id,
    method: 'get',
    params: { accessKey: data.accessKey }
  })
}

export function reportShareDetailByCode(data) {
  return request({
    url: 'reportShare/detailByCode',
    method: 'get',
    params: { shareCode: data }
  })
}

export default { reportShareList, reportShareAdd, reportShareDeleteBatch, reportShareUpdate, reportShareDetail }
