package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.SqBehaviorMonitor;

public interface SqInclinedShaftBehaviorMapper
{
    public SqBehaviorMonitor selectSqInclinedShaftBehaviorById(Long id);
    public List<SqBehaviorMonitor> selectSqInclinedShaftBehaviorList(SqBehaviorMonitor behavior);
    public int insertSqInclinedShaftBehavior(SqBehaviorMonitor behavior);
    public int updateSqInclinedShaftBehavior(SqBehaviorMonitor behavior);
    public int deleteSqInclinedShaftBehaviorByIds(Long[] ids);
}
