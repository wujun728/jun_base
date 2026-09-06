package com.aidex.system.mapper;

import com.aidex.common.core.mapper.BaseMapper;
import com.aidex.system.domain.SysOperLog;
import org.apache.ibatis.annotations.Param;

/**
 * 操作日志 数据层
 * 
 * @author ruoyi
 */
public interface SysOperLogMapper extends BaseMapper<SysOperLog>
{


    /**
     * 批量删除系统操作日志
     * 
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    public int deleteOperLogByIds(@Param("operIds") String[] operIds, @Param("DEL_FLAG_DELETE") String DEL_FLAG_DELETE);


    /**
     * 清空操作日志
     */
    public int cleanOperLog();


    public int addOperlog(SysOperLog sysOperLog);

    public int deleteOperLogBySaveDay(@Param("saveDay") String saveDay );
}
