import type { TreeSelect } from '@/types/api/system/common'
import type {
  MenuQueryParams,
  MenuSortParams,
  SysMenu
} from '@/types/api/system/menu'

import response from '@/utils/http'

// 查询菜单列表
export function listMenu(query?: MenuQueryParams) {
  return response<SysMenu[]>({
    url: '/system/menu/list',
    method: 'get',
    params: query
  })
}

// 查询菜单详细
export function getMenu(menuId: number) {
  return response<SysMenu>({
    url: '/system/menu/' + menuId,
    method: 'get'
  })
}

// 查询菜单下拉树结构
export function treeselect() {
  return response<TreeSelect[]>({
    url: '/system/menu/treeselect',
    method: 'get'
  })
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeselect(roleId: number) {
  return response<TreeSelect[]>({
    url: '/system/menu/roleMenuTreeselect/' + roleId,
    method: 'get'
  })
}

// 新增菜单
export function addMenu(data: SysMenu) {
  return response({
    url: '/system/menu',
    method: 'post',
    data: data
  })
}

// 修改菜单
export function updateMenu(data: SysMenu) {
  return response({
    url: '/system/menu',
    method: 'put',
    data: data
  })
}

// 保存菜单排序
export function updateMenuSort(data: MenuSortParams) {
  return response({
    url: '/system/menu/updateSort',
    method: 'put',
    data: data
  })
}

// 删除菜单
export function delMenu(data: SysMenu) {
  return response({
    url: '/system/menu/' + data.menuId,
    method: 'delete'
  })
}
