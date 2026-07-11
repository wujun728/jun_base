import request from '@/utils/request'

// 查询流程办理秘书列表
export function listSecretary(query) {
  return request({
    url: '/worksetting/secretary/list',
    method: 'get',
    params: query
  })
}

// 查询流程办理秘书详细
export function getSecretary(id) {
  return request({
    url: '/worksetting/secretary/' + id,
    method: 'get'
  })
}

// 新增流程办理秘书
export function addSecretary(data) {
  return request({
    url: '/worksetting/secretary',
    method: 'post',
    data: data
  })
}

// 修改流程办理秘书
export function updateSecretary(data) {
  return request({
    url: '/worksetting/secretary',
    method: 'put',
    data: data
  })
}

// 删除流程办理秘书
export function delSecretary(id) {
  return request({
    url: '/worksetting/secretary/' + id,
    method: 'delete'
  })
}

// 启、停配置
export function changeEnableFlag(id, enableFlag) {
  const data = {
    id,
    enableFlag
  }
  return request({
    url: '/worksetting/secretary/changeEnableFlag',
    method: 'put',
    data: data
  })
}