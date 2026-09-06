package com.geek.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Update;

import com.geek.system.domain.SysOperLog;
import com.mybatisflex.core.BaseMapper;

/**
 * 操作日志 数据层
 * 
 * @author geek
 */
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {

    /**
     * 清空操作日志
     */
    @Update("truncate table sys_oper_log")
    public void cleanOperLog();

    /**
     * 获取成功操作的统计信息
     */
    public List<Map<String, Object>> getSuccessOperationStats(SysOperLog operLog);

    /**
     * 获取失败操作的统计信息
     */
    public List<Map<String, Object>> getFailureOperationStats(SysOperLog operLog);

    /**
     * 获取按状态分类的操作统计信息
     */
    public List<Map<String, Object>> getStatusStats(SysOperLog operLog);

    /**
     * 获取按模块和操作类型分类的操作统计信息
     */
    public List<Map<String, Object>> getModuleOperationStats(SysOperLog operLog);
}
