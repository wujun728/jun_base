import request from '@/utils/request'

export function getAllProjectList(query) {
  return request({
    url: '/qixing/common/getAllProjectList',
    method: 'get',
    params: query
  })
}

export function getAllProjectDetail(query) {
  return request({
    url: '/qixing/common/getAllProjectDetail',
    method: 'get',
    params: query
  })
}

export function getAllProjectCount(query) {
  return request({
    url: '/qixing/common/getAllProjectCount',
    method: 'get',
    params: query
  })
}

export function getCustomerProjects(query) {
  return request({
    url: '/qixing/common/getCustomerProjects',
    method: 'get',
    params: query
  })
}

export function getProjectState(query) {
  return request({
    url: '/qixing/common/getProjectState',
    method: 'get',
    params: query
  })
}

export function getPageIndexInfoList(query) {
  return request({
    url: '/qixing/common/getPageIndexInfoList',
    method: 'get',
    params: query
  })
}

export function getDictDetailList(code) {
  return request({
    url: '/qixing/common/getDictDetailList/' + code,
    method: 'get'
  })
}

export function getEditorInfoList(query) {
  return request({
    url: '/qixing/common/getEditorInfoList',
    method: 'get',
    params: query
  })
}

export function getCodeByType(type) {
  return request({
    url: '/qixing/common/getCodeByType/' + type,
    method: 'get'
  })
}

export function getProjectMemberList(code) {
  return request({
    url: '/qixing/common/getProjectMemberList/' + code,
    method: 'get'
  })
}

export function getWfHisTaskActors(orderId) {
  return request({
    url: '/qixing/common/getWfHisTaskActors/' + orderId,
    method: 'get'
  })
}

export function getFlowableHistoryTasks(procInsId) {
  return request({
    url: '/qixing/common/getFlowableHistoryTasks/' + procInsId,
    method: 'get'
  })
}

export function submitFlow(data) {
  return request({
    url: '/qixing/common/submitFlow',
    method: 'post',
    data: data
  })
}

export function queryCustomerIdByProjectId(id) {
  return request({
    url: '/qixing/common/queryCustomerIdByProjectId/' + id,
    method: 'get'
  })
}

export function queryContractIdByProjectId(id) {
  return request({
    url: '/qixing/common/queryContractIdByProjectId/' + id,
    method: 'get'
  })
}

export function queryPlanIdByProjectId(id) {
  return request({
    url: '/qixing/common/queryPlanIdByProjectId/' + id,
    method: 'get'
  })
}

export function queryDraftIdByProjectId(id) {
  return request({
    url: '/qixing/common/queryDraftIdByProjectId/' + id,
    method: 'get'
  })
}

export function queryReportIdByProjectId(id) {
  return request({
    url: '/qixing/common/queryReportIdByProjectId/' + id,
    method: 'get'
  })
}