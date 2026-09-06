package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.SqDispatchReport;
import com.ruoyi.business.domain.SqSafetyEventStat;

public interface SqReportMapper
{
    public SqDispatchReport selectDispatchReportById(Long id);
    public List<SqDispatchReport> selectDispatchReportList(SqDispatchReport report);
    public List<SqDispatchReport> selectDispatchMonthReportList(SqDispatchReport report);
    public List<SqSafetyEventStat> selectSafetyEventStatList(SqSafetyEventStat stat);
}
