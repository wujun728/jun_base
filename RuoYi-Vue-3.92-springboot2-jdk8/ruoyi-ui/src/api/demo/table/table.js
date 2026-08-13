import request from '@/utils/request'

// 查询综合表格列表
export function listTable(query) {
  return request({
    url: '/demo/table/list',
    method: 'get',
    params: query
  })
}

// 查询综合表格详细
export function getTable(id) {
  return request({
    url: '/demo/table/' + id,
    method: 'get'
  })
}

// 新增综合表格
export function addTable(data) {
  return request({
    url: '/demo/table',
    method: 'post',
    data: data
  })
}

// 修改综合表格
export function updateTable(data) {
  return request({
    url: '/demo/table',
    method: 'put',
    data: data
  })
}

// 删除综合表格
export function delTable(id) {
  return request({
    url: '/demo/table/' + id,
    method: 'delete'
  })
}

// 导出综合表格
export function exportTable(query) {
  return request({
    url: '/demo/table/export',
    method: 'get',
    params: query
  })
}
