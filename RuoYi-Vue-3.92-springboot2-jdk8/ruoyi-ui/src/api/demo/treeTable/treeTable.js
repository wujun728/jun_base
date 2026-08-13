import request from '@/utils/request'

// 查询树形表格列表
export function listTreeTable(query) {
  return request({
    url: '/demo/treeTable/list',
    method: 'get',
    params: query
  })
}

// 查询树形表格详细
export function getTreeTable(id) {
  return request({
    url: '/demo/treeTable/' + id,
    method: 'get'
  })
}

// 新增树形表格
export function addTreeTable(data) {
  return request({
    url: '/demo/treeTable',
    method: 'post',
    data: data
  })
}

// 修改树形表格
export function updateTreeTable(data) {
  return request({
    url: '/demo/treeTable',
    method: 'put',
    data: data
  })
}

// 删除树形表格
export function delTreeTable(id) {
  return request({
    url: '/demo/treeTable/' + id,
    method: 'delete'
  })
}

// 导出树形表格
export function exportTreeTable(query) {
  return request({
    url: '/demo/treeTable/export',
    method: 'get',
    params: query
  })
}
