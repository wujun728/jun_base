package com.aidex.system.service;

import com.aidex.common.core.service.BaseService;
import com.aidex.system.domain.SysNoticeUserRead;

/**
 * 通知公告用户阅读Service接口
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-07-02
 */
public interface SysNoticeUserReadService extends BaseService<SysNoticeUserRead>
{
    /**
     * 批量删除通知公告用户阅读
     * @param ids 需要删除的通知公告用户阅读ID
     * @return
     */
    public int deleteSysNoticeUserReadByIds(String[] ids);

    public boolean setIsRead(String noticeId);
}
