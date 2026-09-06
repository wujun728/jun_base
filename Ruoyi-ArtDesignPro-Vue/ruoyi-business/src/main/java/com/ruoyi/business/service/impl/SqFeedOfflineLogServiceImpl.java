package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqFeedLog;
import com.ruoyi.business.mapper.SqFeedOfflineLogMapper;
import com.ruoyi.business.service.ISqFeedOfflineLogService;
import com.ruoyi.common.utils.StringUtils;

@Service
public class SqFeedOfflineLogServiceImpl implements ISqFeedOfflineLogService
{
    @Autowired
    private SqFeedOfflineLogMapper sqFeedOfflineLogMapper;

    @Override
    public SqFeedLog selectSqFeedOfflineLogById(Long id)
    {
        return sqFeedOfflineLogMapper.selectSqFeedOfflineLogById(id);
    }

    @Override
    public List<SqFeedLog> selectSqFeedOfflineLogList(SqFeedLog log)
    {
        normalizeDeletedFlag(log);
        return sqFeedOfflineLogMapper.selectSqFeedOfflineLogList(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSqFeedOfflineLog(SqFeedLog log)
    {
        normalizeDeletedFlag(log);
        if (StringUtils.isBlank(log.getStatus()))
        {
            log.setStatus("0");
        }
        return sqFeedOfflineLogMapper.insertSqFeedOfflineLog(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSqFeedOfflineLog(SqFeedLog log)
    {
        return sqFeedOfflineLogMapper.updateSqFeedOfflineLog(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqFeedOfflineLogByIds(Long[] ids)
    {
        return sqFeedOfflineLogMapper.deleteSqFeedOfflineLogByIds(ids);
    }

    private void normalizeDeletedFlag(SqFeedLog log)
    {
        if (StringUtils.isBlank(log.getIsDeleted()))
        {
            log.setIsDeleted("0");
        }
    }
}
