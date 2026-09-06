package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.SqBehaviorMonitor;

public interface SqUserBehaviorMapper
{
    public SqBehaviorMonitor selectSqUserBehaviorById(Long id);
    public List<SqBehaviorMonitor> selectSqUserBehaviorList(SqBehaviorMonitor behavior);
    public int insertSqUserBehavior(SqBehaviorMonitor behavior);
    public int updateSqUserBehavior(SqBehaviorMonitor behavior);
    public int deleteSqUserBehaviorByIds(Long[] ids);
}
