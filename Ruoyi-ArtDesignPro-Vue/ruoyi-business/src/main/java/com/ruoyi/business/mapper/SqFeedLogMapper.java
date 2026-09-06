package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.SqFeedLog;

public interface SqFeedLogMapper
{
    public SqFeedLog selectSqFeedLogById(Long id);
    public List<SqFeedLog> selectSqFeedLogList(SqFeedLog log);
    public int insertSqFeedLog(SqFeedLog log);
    public int updateSqFeedLog(SqFeedLog log);
    public int deleteSqFeedLogByIds(Long[] ids);
}
