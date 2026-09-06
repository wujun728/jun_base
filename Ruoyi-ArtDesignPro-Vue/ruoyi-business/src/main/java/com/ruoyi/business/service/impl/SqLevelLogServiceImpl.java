package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqLevelLog;
import com.ruoyi.business.mapper.SqLevelLogMapper;
import com.ruoyi.business.service.ISqLevelLogService;

@Service
public class SqLevelLogServiceImpl implements ISqLevelLogService
{
    @Autowired
    private SqLevelLogMapper sqLevelLogMapper;

    @Override
    public SqLevelLog selectSqLevelLogById(Long id)
    {
        return sqLevelLogMapper.selectSqLevelLogById(id);
    }

    @Override
    public List<SqLevelLog> selectSqLevelLogList(SqLevelLog log)
    {
        return sqLevelLogMapper.selectSqLevelLogList(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSqLevelLog(SqLevelLog log)
    {
        return sqLevelLogMapper.insertSqLevelLog(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSqLevelLog(SqLevelLog log)
    {
        return sqLevelLogMapper.updateSqLevelLog(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqLevelLogByIds(Long[] ids)
    {
        return sqLevelLogMapper.deleteSqLevelLogByIds(ids);
    }
}
