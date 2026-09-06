package com.aidex.system.mapper;

import com.aidex.common.core.mapper.BaseMapper;
import com.aidex.system.domain.SysLoginLog;
import org.apache.ibatis.annotations.Param;

/**
 * 系统访问日志情况信息 数据层
 * 
 * @author ruoyi
 */
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog>
{

    /**
     * 批量删除系统登录日志
     * 
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    public int deleteLoginLogByIds(@Param("infoIds") String[] infoIds, @Param("DEL_FLAG_DELETE") String DEL_FLAG_DELETE);

    /**
     * 清空系统登录日志
     * 
     * @return 结果
     */
    public int cleanLoginLog();

    /**
     * 保存日志信息
     * @param sysLoginLog
     * @return
     */
    public int addLoginLog(SysLoginLog sysLoginLog);

    /**
     * 根据日志保留时间清除历史日志
     * @param saveDay
     * @return
     */
    public int deleteLoginLogBySaveDay(@Param("saveDay") String saveDay );

}
