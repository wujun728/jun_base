import request from '@/utils/request'

// 我的发起的流程
export function myProcessList(query) {
  return request({
    url: '/flowable/task/myProcess',
    method: 'get',
    params: query
  })
}

export function flowFormData(query) {
  return request({
    url: '/flowable/task/flowFormData',
    method: 'get',
    params: query
  })
}

export function flowTaskInfo(query) {
  return request({
    url: '/flowable/task/flowTaskInfo',
    method: 'get',
    params: query
  })
}

// 完成任务
export function complete(data) {
  return request({
    url: '/flowable/task/complete',
    method: 'post',
    data: data
  })
}

// 取消申请
export function stopProcess(data) {
  return request({
    url: '/flowable/task/stopProcess',
    method: 'post',
    data: data
  })
}

// 驳回任务
export function rejectTask(data) {
  return request({
    url: '/flowable/task/reject',
    method: 'post',
    data: data
  })
}

// 可退回任务列表
export function returnList(data) {
  return request({
    url: '/flowable/task/returnList',
    method: 'post',
    data: data
  })
}

// 部署流程实例
export function deployStart(deployId) {
  return request({
    url: '/flowable/process/startFlow/' + deployId,
    method: 'get',
  })
}
