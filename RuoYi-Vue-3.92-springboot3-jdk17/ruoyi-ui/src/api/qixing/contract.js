import request from '@/utils/request'

// 查询业务约定书列表
export function listContract(query) {
  return request({
    url: '/system/contract/list',
    method: 'get',
    params: query
  })
}

// 查询业务约定书详细
export function getContract(id) {
  return request({
    url: '/system/contract/' + id,
    method: 'get'
  })
}

// 新增业务约定书
export function addContract(data) {
  return request({
    url: '/system/contract',
    method: 'post',
    data: data
  })
}

// 修改业务约定书
export function updateContract(data) {
  return request({
    url: '/system/contract',
    method: 'put',
    data: data
  })
}

// 删除业务约定书
export function delContract(id) {
  return request({
    url: '/system/contract/' + id,
    method: 'delete'
  })
}

// 鏌ヨContract涓嬫媺鍒楄〃
export function listContractSelect(query) {
  return request({
    url: '/system/contract/listBySelect',
    method: 'get',
    params: query
  })
}