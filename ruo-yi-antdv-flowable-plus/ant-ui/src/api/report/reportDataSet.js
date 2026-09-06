import request from '@/utils/request'

export function reportDataSetList(params) {
  return request({
    url: 'dataSet/pageList',
    method: 'GET',
    params,
  })
}

export function reportDataSetAdd(data) {
  return request({
    url: 'dataSet',
    method: 'post',
    data
  })
}

export function reportDataSetDeleteBatch(data) {
  return request({
    url: 'dataSet/delete/batch',
    method: 'post',
    data
  })
}

export function reportDataSetUpdate(data) {
  return request({
    url: 'dataSet',
    method: 'put', data,
  })
}

export function reportDataSetDetail(data) {
  return request({
    url: 'dataSet/' + data.id,
    method: 'get',
    params: { accessKey: data.accessKey }
  })
}

export default { reportDataSetList, reportDataSetAdd, reportDataSetDeleteBatch, reportDataSetUpdate, reportDataSetDetail }
