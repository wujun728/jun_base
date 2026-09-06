package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqOutLog;
import com.ruoyi.business.mapper.SqOutLogMapper;
import com.ruoyi.business.service.ISqOutLogService;

@Service
public class SqOutLogServiceImpl implements ISqOutLogService
{
    @Autowired
    private SqOutLogMapper sqOutLogMapper;

    @Override
    public SqOutLog selectSqOutLogById(Long id)
    {
        return sqOutLogMapper.selectSqOutLogById(id);
    }

    @Override
    public List<SqOutLog> selectSqOutLogList(SqOutLog log)
    {
        return sqOutLogMapper.selectSqOutLogList(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSqOutLog(SqOutLog log)
    {
        return sqOutLogMapper.insertSqOutLog(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSqOutLog(SqOutLog log)
    {
        return sqOutLogMapper.updateSqOutLog(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqOutLogByIds(Long[] ids)
    {
        return sqOutLogMapper.deleteSqOutLogByIds(ids);
    }
}
