import request from '@/utils/request'

// 完成任务
export function complete(data) {
  return request({
    url: '/workflow/handle/complete',
    method: 'post',
    data: data
  })
}

// 委派任务
export function delegateTask(data) {
  return request({
    url: '/workflow/handle/delegate',
    method: 'post',
    data: data
  })
}

// 转办任务
export function assignTask(data) {
  return request({
    url: '/workflow/handle/assign',
    method: 'post',
    data: data
  })
}

// 认领/签收任务
export function claimTask(data) {
  return request({
    url: '/workflow/handle/claim',
    method: 'post',
    data: data
  })
}

// 取消认领/签收任务
export function unClaimTask(data) {
  return request({
    url: '/workflow/handle/unClaim',
    method: 'post',
    data: data
  })
}

// 退回任务
export function returnTask(data) {
  return request({
    url: '/workflow/handle/return',
    method: 'post',
    data: data
  })
}

// 驳回任务
export function rejectTask(data) {
  return request({
    url: '/workflow/handle/reject',
    method: 'post',
    data: data
  })
}

// 多实例加签
export function addMultiTask(data) {
  return request({
    url: '/workflow/handle/addMultiInstanceExecution',
    method: 'post',
    data: data
  })
}

// 多实例减签
export function deleteMultiInstanceExecution(data) {
  return request({
    url: '/workflow/handle/deleteMultiInstanceExecution',
    method: 'post',
    data: data
  })
}

// 抄送任务
export function copyTask(data) {
  return request({
    url: '/workflow/handle/copyTask',
    method: 'post',
    data: data
  })
}

// 可退回任务列表
export function returnList(data) {
  return request({
    url: '/workflow/handle/returnList',
    method: 'post',
    data: data
  })
}

// 下一节点
export function getNextFlowNode(data) {
  return request({
    url: '/workflow/handle/nextFlowNode',
    method: 'post',
    data: data
  })
}

// 流程节点表单
export function flowTaskForm(query) {
  return request({
    url: '/workflow/handle/flowTaskForm',
    method: 'get',
    params: query
  })
}

// 任务流转记录
export function flowRecord(data) {
  return request({
    url: '/workflow/handle/flowRecord',
    method: 'post',
    data: data
  })
}

// 流程审批意见
export function flowCmts(data) {
  return request({
    url: '/workflow/handle/flowCmts',
    method: 'post',
    data: data
  })
}

// 取回
export function revokeProcess(data) {
  return request({
    url: '/workflow/handle/revokeProcess',
    method: 'post',
    data: data
  })
}

// 终止办理
export function finishProcess(data) {
  return request({
    url: '/workflow/handle/finishProcess',
    method: 'post',
    data: data
  })
}

// 催办
export function urge(data) {
  return request({
    url: '/workflow/handle/urge',
    method: 'post',
    data: data
  })
}

// 获取任务
export function getTask(query) {
  return request({
    url: '/workflow/handle/info',
    method: 'get',
    params: query
  })
}

// 获取历史任务
export function getHistoryTask(query) {
  return request({
    url: '/workflow/handle/history/info',
    method: 'get',
    params: query
  })
}

// 获取流程变量
export function getProcessVariables(taskId) {
  return request({
    url: '/workflow/handle/processVariables/' + taskId,
    method: 'get'
  })
}

// 获取流程执行节点
export function getFlowViewer(procInsId, executionId) {
  return request({
    url: '/workflow/handle/flowViewer/' + procInsId + '/' + executionId,
    method: 'get'
  })
}

// 流程节点数据
export function flowXmlAndNode(query) {
  return request({
    url: '/workflow/handle/flowXmlAndNode',
    method: 'get',
    params: query
  })
}

// 取回提交
export function returnSubmit(data) {
  return request({
    url: '/workflow/handle/returnSubmit',
    method: 'post',
    data: data
  })
}

