package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SqLevelLog;

public interface ISqLevelLogService
{
    public SqLevelLog selectSqLevelLogById(Long id);
    public List<SqLevelLog> selectSqLevelLogList(SqLevelLog log);
    public int insertSqLevelLog(SqLevelLog log);
    public int updateSqLevelLog(SqLevelLog log);
    public int deleteSqLevelLogByIds(Long[] ids);
}
