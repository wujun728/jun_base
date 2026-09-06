package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SqFeedLog;

public interface ISqFeedOfflineLogService
{
    public SqFeedLog selectSqFeedOfflineLogById(Long id);
    public List<SqFeedLog> selectSqFeedOfflineLogList(SqFeedLog log);
    public int insertSqFeedOfflineLog(SqFeedLog log);
    public int updateSqFeedOfflineLog(SqFeedLog log);
    public int deleteSqFeedOfflineLogByIds(Long[] ids);
}
