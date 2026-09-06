import type { DeptQueryParams, DeptSortParams, SysDept } from '@/types/api'

import response from '@/utils/http'

// 查询部门列表
export function listDept(query?: DeptQueryParams) {
  return response<SysDept[]>({
    url: '/system/dept/list',
    method: 'get',
    params: query
  })
}

// 查询部门详细
export function getDept(deptId: number) {
  return response<SysDept>({
    url: '/system/dept/' + deptId,
    method: 'get'
  })
}

// 新增部门
export function addDept(data: SysDept) {
  return response({
    url: '/system/dept',
    method: 'post',
    data
  })
}

// 修改部门
export function updateDept(data: SysDept) {
  return response({
    url: '/system/dept',
    method: 'put',
    data
  })
}

// 保存部门排序
export function updateDeptSort(data: DeptSortParams) {
  return response({
    url: '/system/dept/updateSort',
    method: 'put',
    data
  })
}

// 删除部门
export function delDept(data: SysDept) {
  return response({
    url: '/system/dept/' + data.deptId,
    method: 'delete'
  })
}

// 查询部门列表（排除指定部门及其子部门）
export function listDeptExcludeChild(deptId: number) {
  return response<SysDept[]>({
    url: '/system/dept/list/exclude/' + deptId,
    method: 'get'
  })
}
