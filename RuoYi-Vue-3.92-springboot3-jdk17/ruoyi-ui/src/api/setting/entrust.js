import request from '@/utils/request'

// 查询流程办理委托列表
export function listEntrust(query) {
  return request({
    url: '/worksetting/entrust/list',
    method: 'get',
    params: query
  })
}

// 查询流程办理委托详细
export function getEntrust(id) {
  return request({
    url: '/worksetting/entrust/' + id,
    method: 'get'
  })
}

// 新增流程办理委托
export function addEntrust(data) {
  return request({
    url: '/worksetting/entrust',
    method: 'post',
    data: data
  })
}

// 修改流程办理委托
export function updateEntrust(data) {
  return request({
    url: '/worksetting/entrust',
    method: 'put',
    data: data
  })
}

// 删除流程办理委托
export function delEntrust(id) {
  return request({
    url: '/worksetting/entrust/' + id,
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
    url: '/worksetting/entrust/changeEnableFlag',
    method: 'put',
    data: data
  })
}
