package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SqDispatchReport;
import com.ruoyi.business.domain.SqSafetyEventStat;

public interface ISqReportService
{
    public SqDispatchReport selectDispatchReportById(Long id);
    public List<SqDispatchReport> selectDispatchReportList(SqDispatchReport report);
    public List<SqDispatchReport> selectDispatchMonthReportList(SqDispatchReport report);
    public List<SqSafetyEventStat> selectSafetyEventStatList(SqSafetyEventStat stat);
}
