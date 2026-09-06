package com.geek.system.service;

import java.util.List;
import java.util.Map;

import com.geek.system.domain.SysOperLog;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 操作日志 服务层
 * 
 * @author geek
 */
public interface ISysOperLogService extends IService<SysOperLog> {

    /**
     * 清空操作日志
     */
    public void cleanOperLog();

    /**
     * 获取成功操作的统计信息
     */
    List<Map<String, Object>> getSuccessOperationStats(SysOperLog operLog);

    /**
     * 获取失败操作的统计信息
     */
    List<Map<String, Object>> getFailureOperationStats(SysOperLog operLog);

    /**
     * 获取按状态分类的操作统计信息
     */
    List<Map<String, Object>> getStatusStats(SysOperLog operLog);

    /**
     * 获取按模块和操作类型分类的操作统计信息
     */
    List<Map<String, Object>> getModuleOperationStats(SysOperLog operLog);

    Page<SysOperLog> page(SysOperLog operLog, int pageNum, int pageSize);

    void export(SysOperLog operLog, HttpServletResponse response);
}
