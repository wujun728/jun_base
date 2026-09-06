package com.ruoyi.system.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.constant.SqlConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.mapper.SysOperLogMapper;
import com.ruoyi.system.service.ISysOperLogService;

/**
 * 操作日志 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService
{
    @Autowired
    private SysOperLogMapper operLogMapper;

    /**
     * 新增操作日志
     * 
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(SysOperLog operLog)
    {
        operLogMapper.insert(operLog);
    }

    /**
     * 查询系统操作日志集合
     * 
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public IPage<SysOperLog> selectOperLogList(IPage<SysOperLog> page, SysOperLog operLog)
    {
        LambdaQueryWrapper<SysOperLog> query = new QueryWrapper<SysOperLog>().lambda();
        query.like(StringUtils.isNotEmpty(operLog.getTitle()), SysOperLog::getTitle, operLog.getTitle());
        query.eq(StringUtils.isNotNull(operLog.getBusinessType()), SysOperLog::getBusinessType, operLog.getBusinessType());
        query.in(StringUtils.isNotNull(operLog.getBusinessTypes()), SysOperLog::getBusinessType, operLog.getBusinessTypes());
        query.eq(StringUtils.isNotNull(operLog.getStatus()), SysOperLog::getStatus, operLog.getStatus());
        query.like(StringUtils.isNotEmpty(operLog.getOperName()), SysOperLog::getOperName, operLog.getOperName());

        if (StringUtils.isNotNull(operLog.getParams()) && StringUtils.isNotNull(operLog.getParams().get(SqlConstants.FIELD_NAME_BEGIN_TIME)) && StringUtils.isNotNull(operLog.getParams().get(SqlConstants.FIELD_NAME_END_TIME))) {
            query.between(SysOperLog::getOperTime, operLog.getParams().get(SqlConstants.FIELD_NAME_BEGIN_TIME), operLog.getParams().get(SqlConstants.FIELD_NAME_END_TIME));
        }

        query.orderByDesc(SysOperLog::getOperId);

        return operLogMapper.selectPage(page, query);
    }

    /**
     * 批量删除系统操作日志
     * 
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds)
    {
        return operLogMapper.deleteBatchIds(Arrays.asList(operIds));
    }

    /**
     * 查询操作日志详细
     * 
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLog selectOperLogById(Long operId)
    {
        return operLogMapper.selectById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog()
    {
        operLogMapper.delete(new QueryWrapper<SysOperLog>().lambda().eq(SysOperLog::getStatus, UserConstants.NORMAL).or().eq(SysOperLog::getStatus, UserConstants.EXCEPTION));
    }
}
