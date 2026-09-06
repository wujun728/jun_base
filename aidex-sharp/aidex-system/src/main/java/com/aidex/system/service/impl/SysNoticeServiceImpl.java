package com.aidex.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.aidex.common.core.domain.BaseEntity;
import com.aidex.common.core.page.PageDomain;
import com.aidex.common.core.service.BaseServiceImpl;
import com.aidex.system.domain.SysNotice;
import com.aidex.system.mapper.SysNoticeMapper;
import com.aidex.system.service.SysNoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 公告 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class SysNoticeServiceImpl extends BaseServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService
{
    /**
     * 批量删除公告信息
     * 
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false)
    public int deleteNoticeByIds(String[] noticeIds)
    {
        return mapper.deleteNoticeByIds(noticeIds, BaseEntity.DEL_FLAG_DELETE);
    }
    @Override
    public List<SysNotice> getNoticeListByUserId(String userId){
        return mapper.getNoticeListByUserId(userId, BaseEntity.DEL_FLAG_NORMAL);
    }
    @Override
    public PageInfo<SysNotice> findNoticeByUserPage(SysNotice sysNotice){
        PageDomain page = sysNotice.getPage();
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), page.getOrderBy());
        return new PageInfo(mapper.findNoticeByUserList(sysNotice));
    }
}
