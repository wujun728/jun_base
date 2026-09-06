import type { AjaxResult } from './common'

/** 通知公告分页查询参数 */
export interface NoticeQueryParams {
  /** 公告标题 */
  noticeTitle?: string
  /** 操作人员 */
  createBy?: string
  /** 公告类型 */
  noticeType?: string
  /** 状态 */
  status?: string
  /** 页码 */
  pageNum?: number
  /** 每页条数 */
  pageSize?: number
}

/** 通知公告信息 */
export interface SysNotice {
  /** 公告编号 */
  noticeId?: number
  /** 公告标题 */
  noticeTitle?: string
  /** 公告类型（1通知 2公告） */
  noticeType?: '1' | '2'
  /** 公告内容 */
  noticeContent?: string
  /** 状态（0正常 1停用） */
  status?: '0' | '1'
  /** 创建者 */
  createBy?: string
  /** 创建时间 */
  createTime?: string
  /** 备注 */
  remark?: string
  /** 是否已读 */
  isRead?: boolean
}

export interface SysNoticeTopResult extends AjaxResult<SysNotice[]> {
  unreadCount: number
}

/** 公告已读用户查询参数 */
export interface NoticeReadUserQueryParams {
  /** 公告编号 */
  noticeId?: number
  /** 关键字（登录名/用户名） */
  searchValue?: string
  /** 页码 */
  pageNum?: number
  /** 每页条数 */
  pageSize?: number
}

/** 公告已读用户 */
export interface NoticeReadUser {
  userId?: number
  userName?: string
  nickName?: string
  deptName?: string
  phonenumber?: string
  readTime?: string
}
