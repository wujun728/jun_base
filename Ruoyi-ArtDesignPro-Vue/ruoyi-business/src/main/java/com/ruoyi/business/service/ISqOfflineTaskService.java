package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SqOfflineTask;

public interface ISqOfflineTaskService
{
    public SqOfflineTask selectSqOfflineTaskById(Long id);
    public List<SqOfflineTask> selectSqOfflineTaskList(SqOfflineTask task);
    public int insertSqOfflineTask(SqOfflineTask task);
    public int updateSqOfflineTask(SqOfflineTask task);
    public int updateSqOfflineTaskStatus(SqOfflineTask task);
    public int deleteSqOfflineTaskByIds(Long[] ids);
}
