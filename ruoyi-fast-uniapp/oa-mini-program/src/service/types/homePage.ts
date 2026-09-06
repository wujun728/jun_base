/**
 * SysAnnouncementHomepageDto对象，首页消息提醒请求参数
 */
export interface HomePage {
    /**
     * 公告id
     */
    id?: number;
    /**
     * 页数
     */
    pageNum: number;
    /**
     * 每页数量
     */
    pageSize: number;
    /**
     * 已读未读（0：未读，1：已读）
     */
    status?: string;
    /**
     * 类型（1：温馨提示，2：内部通知）
     */
    type?: string;
    total?:number
}