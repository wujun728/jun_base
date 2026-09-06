/*
 * @Author: qianlishi
 * @Date: 2020-07-13 15:13:37
 * @Last Modified by: qianlishi
 * @Last Modified time: 2021-03-04 10:46:26
 */
import request from '@/utils/request'

// resultset
export function dataSetPreview (data) {
  return request({
    url: `/dataSet/detailBysetId/${data.id}`,
    method: 'get',
  })
}

export function addDataSet (data) {
  return request({
    url: '/dataSet',
    method: 'post',
    data,
  })
}
export function editDataSet (data) {
  return request({
    url: '/dataSet',
    method: 'put',
    data,
  })
}
// delete dataset
export function deleteDataSet (data) {
  return request({
    url: '/dataSet/' + data.id,
    method: 'delete',
    data,
  })
}
// 下拉数据源
export function queryAllDataSourceSet (data) {
  return request({
    url: '/dataSource/queryAllDataSource',
    method: 'get',
    data,
  })
}
// 数据集高级规则js验证
export function verificationSet (data) {
  return request({
    url: '/dataSetParam/verification',
    method: 'post',
    data,
  })
}
// 测试数据转换，以及返回数据table列表
export function testTransformSet (data) {
  return request({
    url: '/dataSet/testTransform',
    method: 'post',
    data,
  })
}

// report
export function reportPageList (params) {
  return request({
    url: '/report/pageList',
    method: 'get',
    params,
  })
}
// report
export function addReport (data) {
  return request({
    url: '/report',
    method: 'post',
    data,
  })
}

// report
export function editReport (data) {
  return request({
    url: '/report',
    method: 'put',
    data,
  })
}

// report
export function delReport (data) {
  return request({
    url: '/report/delReport',
    method: 'delete',
    data,
  })
}

// report
export function detailReport (id, accessKey) {
  return request({
    url: `/report/${id}?accessKey=${accessKey}`,
    method: 'get',
  })
}

// reportExcel
export function addReportExcel (data) {
  return request({
    url: '/reportExcel',
    method: 'post',
    data,
  })
}

// reportExcel
export function editReportExcel (data) {
  return request({
    url: '/reportExcel',
    method: 'put',
    data,
  })
}
// /dataSet/pageList
export function dataSetPageList (params) {
  return request({
    url: '/dataSet/pageList',
    method: 'GET',
    params,
  })
}
