package com.aidex.system.service.impl;

import com.aidex.common.core.domain.BaseEntity;
import com.aidex.common.core.service.BaseServiceImpl;
import com.aidex.common.utils.uuid.IdUtils;
import com.aidex.system.domain.SysOperLog;
import com.aidex.system.mapper.SysOperLogMapper;
import com.aidex.system.service.SysOperLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 操作日志 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysOperLogServiceImpl extends BaseServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService
{


    /**
     * 批量删除系统操作日志
     * 
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false)
    public int deleteOperLogByIds(String[] operIds)
    {
        return mapper.deleteOperLogByIds(operIds, BaseEntity.DEL_FLAG_DELETE);
    }

    /**
     * 清空操作日志
     */
    @Override
    @Transactional(readOnly = false)
    public void cleanOperLog()
    {
        mapper.cleanOperLog();
    }

    @Override
    @Transactional(readOnly = false)
    public boolean addOperlog(SysOperLog sysOperLog) {
        sysOperLog.setId(IdUtils.randomUUID());
        return mapper.addOperlog(sysOperLog) > 0 ? true :false;
    }

    /**
     * 根据日志保留时间清除历史日志
     * @param saveDay
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public int deleteOperLogBySaveDay(String saveDay)
    {
        return mapper.deleteOperLogBySaveDay(saveDay);
    }
}
