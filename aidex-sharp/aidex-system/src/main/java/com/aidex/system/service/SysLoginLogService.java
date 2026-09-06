package com.aidex.system.service;

import com.aidex.common.core.service.BaseService;
import com.aidex.system.domain.SysLoginLog;

/**
 * 系统访问日志情况信息 服务层
 * 
 * @author ruoyi
 */
public interface SysLoginLogService extends BaseService<SysLoginLog>
{
    /**
     * 批量删除系统登录日志
     * 
     * @param infoIds 需要删除的登录日志ID
     * @return
     */
    public int deleteLoginLogByIds(String[] infoIds);

    /**
     * 清空系统登录日志
     */
    public void cleanLoginLog();

    /**
     * 新增日志
     * @param sysLoginLog
     * @return
     */
    public boolean addLoginLog(SysLoginLog sysLoginLog);

    /**
     * 根据日志保留时间清除历史日志
     * @param saveDay
     * @return
     */
    public int deleteLoginLogBySaveDay(String saveDay);
}
