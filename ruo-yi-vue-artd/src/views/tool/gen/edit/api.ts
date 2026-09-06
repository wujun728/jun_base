import type { SysDictType } from '@/types/api/system/dict'
import type { MenuQueryParams, SysMenu } from '@/types/api/system/menu'
import type { GenTableInfoResult, GenTable } from '@/types/api/tool/gen'

import response from '@/utils/http'

// 查询表详细信息
export function getGenTable(tableId: number) {
  return response<GenTableInfoResult>({
    url: '/tool/gen/' + tableId,
    method: 'get'
  })
}

// 修改代码生成信息
export function updateGenTable(data: GenTable) {
  return response({
    url: '/tool/gen',
    method: 'put',
    data: data
  })
}

// 获取字典选择框列表
export function optionselect() {
  return response<SysDictType[]>({
    url: '/system/dict/type/optionselect',
    method: 'get'
  })
}

// 查询菜单列表
export function listMenu(query?: MenuQueryParams) {
  return response<SysMenu[]>({
    url: '/system/menu/list',
    method: 'get',
    params: query
  })
}
