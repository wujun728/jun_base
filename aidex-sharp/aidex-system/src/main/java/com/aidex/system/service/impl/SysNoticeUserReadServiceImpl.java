package com.aidex.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.aidex.common.core.domain.model.LoginUser;
import com.aidex.common.core.service.BaseServiceImpl;
import com.aidex.common.utils.SecurityUtils;
import com.aidex.system.domain.SysNoticeUserRead;
import com.aidex.system.mapper.SysNoticeUserReadMapper;
import com.aidex.system.service.SysNoticeUserReadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 通知公告用户阅读Service业务层处理
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-07-02
 */
@Service
@Transactional(readOnly = true)
public class SysNoticeUserReadServiceImpl extends BaseServiceImpl<SysNoticeUserReadMapper, SysNoticeUserRead> implements SysNoticeUserReadService
{
    private static final Logger log = LoggerFactory.getLogger(SysNoticeUserReadServiceImpl.class);

        /**
     * 获取单条数据
     * @param sysNoticeUserRead
     * @return
     */
    @Override
    public SysNoticeUserRead get(SysNoticeUserRead sysNoticeUserRead)
    {
        SysNoticeUserRead dto = super.get(sysNoticeUserRead);
                                                                                                                                                                                                                                                                                                        return dto;
    }

    /**
     * 获取单条数据
     * @return
     */
    @Override
    public SysNoticeUserRead get(String id)
    {
        SysNoticeUserRead dto = super.get(id);
                                                                                                                                                                                                                                                                                                        return dto;
    }


    /**
     * 查询通知公告用户阅读列表
     * @param sysNoticeUserRead
     * @return
     */
    @Override
    public List<SysNoticeUserRead> findList(SysNoticeUserRead sysNoticeUserRead)
    {
        return super.findList(sysNoticeUserRead);
    }

    /**
     * 分页查询通知公告用户阅读列表
     * @param sysNoticeUserRead
     * @return
     */
    @Override
    public PageInfo<SysNoticeUserRead> findPage(SysNoticeUserRead sysNoticeUserRead)
    {
        return super.findPage(sysNoticeUserRead);
    }

    /**
     * 保存通知公告用户阅读
     * @param sysNoticeUserRead
     * @return
     */
    @Override
    public boolean save(SysNoticeUserRead sysNoticeUserRead)
    {
        return super.save(sysNoticeUserRead);
    }

    /**
     * 删除通知公告用户阅读信息
     * @param sysNoticeUserRead
     * @return
     */
    @Override
    public boolean remove(SysNoticeUserRead sysNoticeUserRead)
    {
        return super.remove(sysNoticeUserRead);
    }

    /**
     * 批量删除通知公告用户阅读
     * @param ids 需要删除的通知公告用户阅读ID
     * @return
     */
    @Transactional(readOnly = false)
    @Override
    public int deleteSysNoticeUserReadByIds(String[] ids)
    {
        return mapper.deleteSysNoticeUserReadByIds(ids);
    }
    @Override
    @Transactional(readOnly = false)
    public boolean setIsRead(String noticeId){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysNoticeUserRead sysNoticeUserRead = new SysNoticeUserRead();
        sysNoticeUserRead.setNoticeId(noticeId);
        sysNoticeUserRead.setUserId(loginUser.getUser().getId());
        sysNoticeUserRead.setIsRead("1");
        sysNoticeUserRead.setStatus("0");
        try{
            //通过外键控制数据不能重复
            return this.save(sysNoticeUserRead);
        }catch (Exception e){
            log.error("阅读信息已存储，无需继续存储");
            return true;
        }
    }

}
