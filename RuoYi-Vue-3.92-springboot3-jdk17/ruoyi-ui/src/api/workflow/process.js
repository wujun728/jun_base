import request from '@/utils/request'

// 启动流程
export const startFlow = data => {
  return request({
    url: '/workflow/handle/startFlow',
    method: 'post',
    data: data
  })
}

// 提交审批
export const commonSubmit = data => {
  return request({
    url: '/biz/flow/submit',
    method: 'post',
    data: data
  })
}

// 跳转环节
export const jumpActivity = data => {
  return request({
    url: '/workflow/handle/jumpActivity',
    method: 'post',
    data: data
  })
}

// 终止流程
export const terminateProcess = data => {
  return request({
    url: '/workflow/handle/admin/terminate',
    method: 'post',
    data: data
  })
}

// 取回任务
export const returnFinishTask = data => {
  return request({
    url: '/workflow/handle/admin/return',
    method: 'post',
    data: data
  })
}

// 校验提交
export const checkCompleteCondition = data => {
  return request({
    url: '/workflow/handle/checkComplete',
    method: 'post',
    data: data
  })
}

// 校验退回
export const checkReturnCondition = data => {
  return request({
    url: '/workflow/handle/checkReturn',
    method: 'post',
    data: data
  })
}

// 校验驳回
export const checkRejectCondition = data => {
  return request({
    url: '/workflow/handle/checkReject',
    method: 'post',
    data: data
  })
}

// 获取减签任务列表
export const getDeleteMultiTasks = query => {
  return request({
    url: '/workflow/handle/deleteMultiTask/list',
    method: 'get',
    params: query
  })
}

// 减签任务
export const deleteMultiTask = data => {
  return request({
    url: '/workflow/handle/deleteMultiInstanceExecution',
    method: 'post',
    data: data
  })
}