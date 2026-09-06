package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.SqOutLog;

public interface SqOutLogMapper
{
    public SqOutLog selectSqOutLogById(Long id);
    public List<SqOutLog> selectSqOutLogList(SqOutLog log);
    public int insertSqOutLog(SqOutLog log);
    public int updateSqOutLog(SqOutLog log);
    public int deleteSqOutLogByIds(Long[] ids);
}
