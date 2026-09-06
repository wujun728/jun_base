package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqFeedLog;
import com.ruoyi.business.mapper.SqFeedLogMapper;
import com.ruoyi.business.service.ISqFeedLogService;
import com.ruoyi.common.utils.StringUtils;

@Service
public class SqFeedLogServiceImpl implements ISqFeedLogService
{
    @Autowired
    private SqFeedLogMapper sqFeedLogMapper;

    @Override
    public SqFeedLog selectSqFeedLogById(Long id)
    {
        return sqFeedLogMapper.selectSqFeedLogById(id);
    }

    @Override
    public List<SqFeedLog> selectSqFeedLogList(SqFeedLog log)
    {
        normalizeDeletedFlag(log);
        return sqFeedLogMapper.selectSqFeedLogList(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSqFeedLog(SqFeedLog log)
    {
        normalizeDeletedFlag(log);
        return sqFeedLogMapper.insertSqFeedLog(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSqFeedLog(SqFeedLog log)
    {
        return sqFeedLogMapper.updateSqFeedLog(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqFeedLogByIds(Long[] ids)
    {
        return sqFeedLogMapper.deleteSqFeedLogByIds(ids);
    }

    private void normalizeDeletedFlag(SqFeedLog log)
    {
        if (StringUtils.isBlank(log.getIsDeleted()))
        {
            log.setIsDeleted("0");
        }
    }
}
