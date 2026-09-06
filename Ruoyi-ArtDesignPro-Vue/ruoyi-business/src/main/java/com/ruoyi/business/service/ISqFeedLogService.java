package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SqFeedLog;

public interface ISqFeedLogService
{
    public SqFeedLog selectSqFeedLogById(Long id);
    public List<SqFeedLog> selectSqFeedLogList(SqFeedLog log);
    public int insertSqFeedLog(SqFeedLog log);
    public int updateSqFeedLog(SqFeedLog log);
    public int deleteSqFeedLogByIds(Long[] ids);
}
