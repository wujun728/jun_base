import type { SysLogininfor } from '@/types/api/monitor/logininfor'
import type { TableDataInfo } from '@/types/api/system/common'

import response from '@/utils/http'

// 查询登录日志列表
export function listLogininfor(query: any) {
  const params = { ...query }
  if (params.dateRange) {
    params.params = {
      beginTime: params.dateRange[0],
      endTime: params.dateRange[1]
    }
    delete params.dateRange
  }
  return response<TableDataInfo<SysLogininfor>>({
    url: '/monitor/logininfor/list',
    method: 'get',
    params
  })
}

// 删除登录日志
export function delLogininfor(infoId: number | number[]) {
  return response({
    url: '/monitor/logininfor/' + infoId,
    method: 'delete'
  })
}

// 解锁用户登录状态
export function unlockLogininfor(userName: string) {
  return response({
    url: '/monitor/logininfor/unlock/' + userName,
    method: 'get'
  })
}

// 清空登录日志
export function cleanLogininfor() {
  return response({
    url: '/monitor/logininfor/clean',
    method: 'delete'
  })
}
