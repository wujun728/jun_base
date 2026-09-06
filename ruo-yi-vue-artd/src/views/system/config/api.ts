import type { TableDataInfo } from '@/types/api/system/common'
import type { ConfigQueryParams, SysConfig } from '@/types/api/system/config'

import response from '@/utils/http'

// 查询参数列表
export function listConfig(query: ConfigQueryParams) {
  return response<TableDataInfo<SysConfig>>({
    url: '/system/config/list',
    method: 'get',
    params: query
  })
}

// 查询参数详细
export function getConfig(configId: number) {
  return response<SysConfig>({
    url: '/system/config/' + configId,
    method: 'get'
  })
}

// 新增参数配置
export function addConfig(data: SysConfig) {
  return response({
    url: '/system/config',
    method: 'post',
    data
  })
}

// 修改参数配置
export function updateConfig(data: SysConfig) {
  return response({
    url: '/system/config',
    method: 'put',
    data
  })
}

// 删除参数配置
export function delConfig(configId: number | number[]) {
  return response({
    url: '/system/config/' + configId,
    method: 'delete'
  })
}

// 刷新参数缓存
export function refreshCache() {
  return response({
    url: '/system/config/refreshCache',
    method: 'delete'
  })
}
