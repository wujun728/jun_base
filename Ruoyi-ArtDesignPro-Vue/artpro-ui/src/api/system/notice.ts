import { add, get, list, remove, update } from './base'
import type { Entity, Query } from './types'
import request from '@/utils/http'

const root = '/system/notice'

export type NoticeType = '1' | '2' | '3'

export interface TopNotice extends Entity {
  noticeId: number
  noticeTitle: string
  noticeType: NoticeType
  createBy?: string
  createTime?: string
  isRead: boolean
}

interface TopNoticeResponse {
  data?: TopNotice[]
  unreadCount?: number
}

export const fetchNoticeList = (params: Query) => list(`${root}/list`, params)
export const fetchNotice = (id: number) => get<Entity>(`${root}/${id}`)
export const addNotice = (data: Entity) => add(root, data)
export const updateNotice = (data: Entity) => update(root, data)
export const removeNotice = (ids: string | number) => remove(`${root}/${ids}`)
export const fetchNoticeReadUsers = (params: Query) => list(`${root}/readUsers/list`, params)
export const fetchTopNotices = (noticeType: NoticeType, silent = false) =>
  request
    .get<TopNoticeResponse>({
      url: `${root}/listTop`,
      params: { noticeType },
      unwrapData: false,
      showErrorMessage: !silent
    })
    .then((response) => ({
      rows: response.data ?? [],
      unreadCount: Number(response.unreadCount ?? 0)
    }))

export const markNoticeRead = (noticeId: number) =>
  request.post<void>({ url: `${root}/markRead?noticeId=${noticeId}` })

export const markAllNoticesRead = () =>
  request.post<void>({ url: `${root}/markReadAll`, showSuccessMessage: true })
