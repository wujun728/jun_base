import request from '@/utils/request'
import { getShareToken, getToken } from '@/utils/auth'
import axios from 'axios'

// 保存大屏设计
export function insertDashboard(data) {
  return request({
    url: 'reportDashboard',
    method: 'post',
    data,
  })
}

// 预览、查询大屏详情
export function detailDashboard(data) {
  return request({
    url: 'reportDashboard/' + data,
    headers: { 'Share-Token': getShareToken(), 'Authorization': getToken() },
    method: 'get',
  })
}

// 数据集查询
export function queryAllDataSet(data) {
  return request({
    url: 'dataSet/queryAllDataSet',
    method: 'get',

  })
}

// 获取数据集信息
export function detailBysetId(data) {
  return request({
    url: 'dataSet/detailBysetId/' + data,
    method: 'get',
  })
}

// 获取动态数据
export function getData(data) {
  return request({
    url: 'reportDashboard/getData',
    method: 'post',
    headers: { 'Share-Token': getShareToken(), 'Authorization': getToken() },
    data,
  })
}

// 导出大屏
export function exportDashboard(data) {
  return new Promise((resolve) =>{
    axios({
      method:'get',
      url: process.env.BASE_API + '/reportDashboard/export',
      headers: { 'Authorization': getToken() },
      params:data,
      responseType:'blob'
    }).then(res =>{
      resolve(res.data)
    }).catch(err =>{
      resolve('error')
    })
  })

}

// 导入大屏
export function importDashboard(data) {
  return request({
    url: 'reportDashboard/import',
    method: 'post',
    data,
  })
}
