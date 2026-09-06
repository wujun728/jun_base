import type { TableDataInfo } from '@/types/api/system/common'
import type {
  NoticeQueryParams,
  NoticeReadUserQueryParams,
  NoticeReadUser,
  SysNotice
} from '@/types/api/system/notice'

import response from '@/utils/http'

// 查询公告列表
export function listNotice(query: NoticeQueryParams) {
  return response<TableDataInfo<SysNotice>>({
    url: '/system/notice/list',
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

// 新增公告
export function addNotice(data: SysNotice) {
  return response({
    url: '/system/notice',
    method: 'post',
    data
  })
}

// 修改公告
export function updateNotice(data: SysNotice) {
  return response({
    url: '/system/notice',
    method: 'put',
    data
  })
}

// 删除公告
export function delNotice(noticeId: number | number[]) {
  return response({
    url: '/system/notice/' + noticeId,
    method: 'delete'
  })
}

// 查询公告已读用户列表
export function listNoticeReadUsers(query: NoticeReadUserQueryParams) {
  return response<TableDataInfo<NoticeReadUser>>({
    url: '/system/notice/readUsers/list',
    method: 'get',
    params: query
  })
}
