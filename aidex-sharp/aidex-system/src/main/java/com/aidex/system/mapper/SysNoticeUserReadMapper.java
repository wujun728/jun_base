package com.aidex.system.mapper;

import com.aidex.common.core.mapper.BaseMapper;
import com.aidex.system.domain.SysNoticeUserRead;

/**
 * 通知公告用户阅读Mapper接口
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-07-02
 */
public interface SysNoticeUserReadMapper extends BaseMapper<SysNoticeUserRead>
{
    /**
     * 批量删除通知公告用户阅读
     * @param ids 需要删除的数据ID
     * @return
     */
    public int deleteSysNoticeUserReadByIds(String[] ids);


}
