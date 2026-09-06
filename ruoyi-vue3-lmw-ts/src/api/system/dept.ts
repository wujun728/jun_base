import { request } from '@/utils/hsj/service/index'

// 查询部门列表
export function listDept(query: anyObj) {
  return request({
    url: '/system/dept/list',
    method: 'get',
    params: query,
  })
}

// 查询部门列表（排除节点）
export function listDeptExcludeChild(deptId: number) {
  return request({
    url: '/system/dept/list/exclude/' + deptId,
    method: 'get',
  })
}
