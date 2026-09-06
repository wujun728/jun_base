import type { SysNotice } from '@/types/api/system/notice'

import response from '@/utils/http'

// 首页顶部公告列表（带已读状态）
export function listNoticeTop() {
  return response<{ data: SysNotice[]; unreadCount: number }>({
    url: '/system/notice/listTop',
    method: 'get'
  })
}
