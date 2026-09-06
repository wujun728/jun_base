import type { TableDataInfo } from '@/types/api/system/common'
import type {
  NoticeReadUser,
  NoticeReadUserQueryParams,
  SysNotice
} from '@/types/api/system/notice'

import response from '@/utils/http'

// 标记公告已读
export function markNoticeRead(noticeId: number) {
  return response({
    url: '/system/notice/markRead',
    method: 'post',
    params: { noticeId }
  })
}

// 批量标记已读
export function markNoticeReadAll(ids: string) {
  return response({
    url: '/system/notice/markReadAll',
    method: 'post',
    params: { ids }
  })
}

// 查询公告已读用户列表
export function listNoticeReadUsers(query: NoticeReadUserQueryParams) {
  return response<TableDataInfo<NoticeReadUser[]>>({
    url: '/system/notice/readUsers/list',
    method: 'get',
    params: query
  })
}

// 查询公告详细
export function getNotice(noticeId: number) {
  return response<SysNotice>({
    url: '/system/notice/' + noticeId,
    method: 'get'
  })
}
