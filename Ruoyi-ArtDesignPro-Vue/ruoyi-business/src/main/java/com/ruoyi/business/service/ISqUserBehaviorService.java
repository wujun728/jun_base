package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SqBehaviorMonitor;

public interface ISqUserBehaviorService
{
    public SqBehaviorMonitor selectSqUserBehaviorById(Long id);
    public List<SqBehaviorMonitor> selectSqUserBehaviorList(SqBehaviorMonitor behavior);
    public int insertSqUserBehavior(SqBehaviorMonitor behavior);
    public int updateSqUserBehavior(SqBehaviorMonitor behavior);
    public int deleteSqUserBehaviorByIds(Long[] ids);
}
