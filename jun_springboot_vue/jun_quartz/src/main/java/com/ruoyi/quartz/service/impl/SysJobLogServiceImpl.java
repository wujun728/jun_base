package com.ruoyi.quartz.service.impl;

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
import com.ruoyi.quartz.domain.SysJobLog;
import com.ruoyi.quartz.mapper.SysJobLogMapper;
import com.ruoyi.quartz.service.ISysJobLogService;

/**
 * 定时任务调度日志信息 服务层
 * 
 * @author ruoyi
 */
@Service
public class SysJobLogServiceImpl implements ISysJobLogService
{
    @Autowired
    private SysJobLogMapper jobLogMapper;

    /**
     * 获取quartz调度器日志的计划任务
     * 
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    @Override
    public IPage<SysJobLog> selectJobLogList(IPage<SysJobLog> page, SysJobLog jobLog)
    {
        LambdaQueryWrapper<SysJobLog> query = new QueryWrapper<SysJobLog>().lambda();
        query.like(StringUtils.isNotEmpty(jobLog.getJobName()), SysJobLog::getJobName, jobLog.getJobName());
        query.eq(StringUtils.isNotEmpty(jobLog.getJobGroup()), SysJobLog::getJobGroup, jobLog.getJobGroup());
        query.eq(StringUtils.isNotEmpty(jobLog.getStatus()), SysJobLog::getStatus, jobLog.getStatus());
        query.like(StringUtils.isNotEmpty(jobLog.getInvokeTarget()), SysJobLog::getInvokeTarget, jobLog.getInvokeTarget());

        if (StringUtils.isNotNull(jobLog.getParams()) && StringUtils.isNotNull(jobLog.getParams().get(SqlConstants.FIELD_NAME_BEGIN_TIME)) && StringUtils.isNotNull(jobLog.getParams().get(SqlConstants.FIELD_NAME_END_TIME))) {
            query.between(SysJobLog::getCreateTime, jobLog.getParams().get(SqlConstants.FIELD_NAME_BEGIN_TIME), jobLog.getParams().get(SqlConstants.FIELD_NAME_END_TIME));
        }
        return jobLogMapper.selectPage(page, query);
    }

    /**
     * 通过调度任务日志ID查询调度信息
     * 
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    @Override
    public SysJobLog selectJobLogById(Long jobLogId)
    {
        return jobLogMapper.selectById(jobLogId);
    }

    /**
     * 新增任务日志
     * 
     * @param jobLog 调度日志信息
     */
    @Override
    public void addJobLog(SysJobLog jobLog)
    {
        jobLogMapper.insert(jobLog);
    }

    /**
     * 批量删除调度日志信息
     * 
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteJobLogByIds(Long[] logIds)
    {
        return jobLogMapper.deleteBatchIds(Arrays.asList(logIds));
    }

    /**
     * 删除任务日志
     * 
     * @param jobId 调度日志ID
     */
    @Override
    public int deleteJobLogById(Long jobId)
    {
        return jobLogMapper.deleteById(jobId);
    }

    /**
     * 清空任务日志
     */
    @Override
    public void cleanJobLog()
    {
        jobLogMapper.delete(new QueryWrapper<SysJobLog>().lambda().eq(SysJobLog::getStatus, UserConstants.NORMAL).or().eq(SysJobLog::getStatus, UserConstants.EXCEPTION));
    }
}
