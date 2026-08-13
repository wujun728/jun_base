import request from '@/utils/request'

// 查询左树右表列表
export function listTreeandtable(query) {
  return request({
    url: '/demo/treeandtable/list',
    method: 'get',
    params: query
  })
}

// 查询左树右表详细
export function getTreeandtable(id) {
  return request({
    url: '/demo/treeandtable/' + id,
    method: 'get'
  })
}

// 新增左树右表
export function addTreeandtable(data) {
  return request({
    url: '/demo/treeandtable',
    method: 'post',
    data: data
  })
}

// 修改左树右表
export function updateTreeandtable(data) {
  return request({
    url: '/demo/treeandtable',
    method: 'put',
    data: data
  })
}

// 删除左树右表
export function delTreeandtable(id) {
  return request({
    url: '/demo/treeandtable/' + id,
    method: 'delete'
  })
}

// 导出左树右表
export function exportTreeandtable(query) {
  return request({
    url: '/demo/treeandtable/export',
    method: 'get',
    params: query
  })
}
