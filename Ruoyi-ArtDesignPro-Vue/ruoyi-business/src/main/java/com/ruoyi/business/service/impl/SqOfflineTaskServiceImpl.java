package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqOfflineTask;
import com.ruoyi.business.mapper.SqOfflineTaskMapper;
import com.ruoyi.business.service.ISqOfflineTaskService;
import com.ruoyi.common.utils.StringUtils;

@Service
public class SqOfflineTaskServiceImpl implements ISqOfflineTaskService
{
    @Autowired
    private SqOfflineTaskMapper sqOfflineTaskMapper;

    @Override
    public SqOfflineTask selectSqOfflineTaskById(Long id)
    {
        return sqOfflineTaskMapper.selectSqOfflineTaskById(id);
    }

    @Override
    public List<SqOfflineTask> selectSqOfflineTaskList(SqOfflineTask task)
    {
        return sqOfflineTaskMapper.selectSqOfflineTaskList(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSqOfflineTask(SqOfflineTask task)
    {
        if (StringUtils.isBlank(task.getStatus()))
        {
            task.setStatus("1");
        }
        return sqOfflineTaskMapper.insertSqOfflineTask(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSqOfflineTask(SqOfflineTask task)
    {
        return sqOfflineTaskMapper.updateSqOfflineTask(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSqOfflineTaskStatus(SqOfflineTask task)
    {
        return sqOfflineTaskMapper.updateSqOfflineTaskStatus(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqOfflineTaskByIds(Long[] ids)
    {
        return sqOfflineTaskMapper.deleteSqOfflineTaskByIds(ids);
    }
}
