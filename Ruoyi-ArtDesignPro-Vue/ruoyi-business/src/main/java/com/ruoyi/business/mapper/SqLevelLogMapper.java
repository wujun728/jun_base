package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.SqLevelLog;

public interface SqLevelLogMapper
{
    public SqLevelLog selectSqLevelLogById(Long id);
    public List<SqLevelLog> selectSqLevelLogList(SqLevelLog log);
    public int insertSqLevelLog(SqLevelLog log);
    public int updateSqLevelLog(SqLevelLog log);
    public int deleteSqLevelLogByIds(Long[] ids);
}
