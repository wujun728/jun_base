package com.geek.system.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.system.domain.SysOperLog;
import com.geek.system.mapper.SysOperLogMapper;
import com.geek.system.service.ISysOperLogService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 操作日志 服务层处理
 * 
 * @author geek
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements ISysOperLogService {
    @Autowired
    private SysOperLogMapper operLogMapper;

    /**
     * 查询系统操作日志集合
     * 
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    private QueryChain<SysOperLog> selectOperLogList(SysOperLog operLog) {
        QueryChain<SysOperLog> queryChain = this.queryChain()
                .like(SysOperLog::getTitle, operLog.getTitle())
                .eq(SysOperLog::getBusinessType, operLog.getBusinessType())
                .eq(SysOperLog::getRequestMethod, operLog.getRequestMethod());
        if (ArrayUtils.isNotEmpty(operLog.getBusinessTypes())) {
            queryChain.in(SysOperLog::getBusinessType, (Object[]) operLog.getBusinessTypes());
        }
        queryChain.eq(SysOperLog::getStatus, operLog.getStatus())
                .like(SysOperLog::getOperName, operLog.getOperName())
                .ge(SysOperLog::getOperTime, operLog.getParams().get("beginTime"))
                .le(SysOperLog::getOperTime, operLog.getParams().get("endTime"))
                .orderBy(SysOperLog::getOperId, false);
        return queryChain;
    }

    @Override
    public Page<SysOperLog> page(SysOperLog operLog, int pageNum, int pageSize) {
        return selectOperLogList(operLog).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(SysOperLog operLog, HttpServletResponse response) {
        List<SysOperLog> list = selectOperLogList(operLog).list();
        ExcelUtil<SysOperLog> util = new ExcelUtil<>(SysOperLog.class);
        util.exportExcel(response, list, "操作日志");
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        operLogMapper.cleanOperLog();
    }

    @Override
    public List<Map<String, Object>> getSuccessOperationStats(SysOperLog operLog) {
        return operLogMapper.getSuccessOperationStats(operLog);
    }

    @Override
    public List<Map<String, Object>> getFailureOperationStats(SysOperLog operLog) {
        return operLogMapper.getFailureOperationStats(operLog);
    }

    @Override
    public List<Map<String, Object>> getStatusStats(SysOperLog operLog) {
        return operLogMapper.getStatusStats(operLog);
    }

    @Override
    public List<Map<String, Object>> getModuleOperationStats(SysOperLog operLog) {
        return operLogMapper.getModuleOperationStats(operLog);
    }
}
