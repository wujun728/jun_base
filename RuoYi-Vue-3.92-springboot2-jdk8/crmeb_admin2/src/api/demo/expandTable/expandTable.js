import request from '@/utils/request'

// 查询展开表格列表
export function listExpandTable(query) {
  return request({
    url: '/demo/expandTable/list',
    method: 'get',
    params: query
  })
}

// 查询展开表格详细
export function getExpandTable(id) {
  return request({
    url: '/demo/expandTable/' + id,
    method: 'get'
  })
}

// 新增展开表格
export function addExpandTable(data) {
  return request({
    url: '/demo/expandTable',
    method: 'post',
    data: data
  })
}

// 修改展开表格
export function updateExpandTable(data) {
  return request({
    url: '/demo/expandTable',
    method: 'put',
    data: data
  })
}

// 删除展开表格
export function delExpandTable(id) {
  return request({
    url: '/demo/expandTable/' + id,
    method: 'delete'
  })
}

// 导出展开表格
export function exportExpandTable(query) {
  return request({
    url: '/demo/expandTable/export',
    method: 'get',
    params: query
  })
}
