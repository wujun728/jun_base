package com.danny.system.service;

import com.github.pagehelper.PageInfo;
import com.danny.common.core.service.BaseService;
import com.danny.system.domain.SysNotice;

import java.util.List;

/**
 * 公告 服务层
 * 
 * @author ruoyi
 */
public interface SysNoticeService extends BaseService<SysNotice>
{
    /**
     * 批量删除公告信息
     * 
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    public int deleteNoticeByIds(String[] noticeIds);


    public List<SysNotice> getNoticeListByUserId(String userId);

    public PageInfo<SysNotice> findNoticeByUserPage(SysNotice sysNotice);
}
