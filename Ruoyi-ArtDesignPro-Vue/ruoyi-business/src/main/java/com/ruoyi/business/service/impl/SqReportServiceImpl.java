package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.domain.SqDispatchReport;
import com.ruoyi.business.domain.SqSafetyEventStat;
import com.ruoyi.business.mapper.SqReportMapper;
import com.ruoyi.business.service.ISqReportService;

@Service
public class SqReportServiceImpl implements ISqReportService
{
    @Autowired
    private SqReportMapper sqReportMapper;

    @Override
    public SqDispatchReport selectDispatchReportById(Long id)
    {
        return sqReportMapper.selectDispatchReportById(id);
    }

    @Override
    public List<SqDispatchReport> selectDispatchReportList(SqDispatchReport report)
    {
        return sqReportMapper.selectDispatchReportList(report);
    }

    @Override
    public List<SqDispatchReport> selectDispatchMonthReportList(SqDispatchReport report)
    {
        return sqReportMapper.selectDispatchMonthReportList(report);
    }

    @Override
    public List<SqSafetyEventStat> selectSafetyEventStatList(SqSafetyEventStat stat)
    {
        return sqReportMapper.selectSafetyEventStatList(stat);
    }
}
