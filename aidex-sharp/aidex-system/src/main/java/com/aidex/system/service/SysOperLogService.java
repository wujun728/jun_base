package com.aidex.system.service;

import com.aidex.common.core.service.BaseService;
import com.aidex.system.domain.SysOperLog;

/**
 * 操作日志 服务层
 * 
 * @author ruoyi
 */
public interface SysOperLogService extends BaseService<SysOperLog>
{

    /**
     * 批量删除系统操作日志
     * 
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    public int deleteOperLogByIds(String[] operIds);


    /**
     * 清空操作日志
     */
    public void cleanOperLog();

    public boolean addOperlog(SysOperLog sysOperLog);

    /**
     * 根据日志保留时间清除历史日志
     * @param saveDay
     * @return
     */
    public int deleteOperLogBySaveDay(String saveDay);

}
