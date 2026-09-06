import type {
  OnlineQueryParams,
  SysUserOnline
} from '@/types/api/monitor/online'
import type { TableDataInfo } from '@/types/api/system/common'

import response from '@/utils/http'

export function list(query: OnlineQueryParams) {
  return response<TableDataInfo<SysUserOnline>>({
    url: '/monitor/online/list',
    method: 'get',
    params: query
  })
}

export function forceLogout(tokenId: string) {
  return response({
    url: `/monitor/online/${tokenId}`,
    method: 'delete'
  })
}
