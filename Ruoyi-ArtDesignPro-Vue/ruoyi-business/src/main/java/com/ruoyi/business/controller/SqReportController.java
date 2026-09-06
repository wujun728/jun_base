package com.ruoyi.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.business.domain.SqDispatchReport;
import com.ruoyi.business.domain.SqSafetyEventStat;
import com.ruoyi.business.service.ISqReportService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/business/sq/report")
public class SqReportController extends BaseController
{
    @Autowired
    private ISqReportService sqReportService;

    @PreAuthorize("@ss.hasAnyPermi('business:dispatchShiftReport:list,business:dispatchDayReport:list')")
    @GetMapping("/dispatch/list")
    public TableDataInfo dispatchList(SqDispatchReport report)
    {
        startPage();
        return getDataTable(sqReportService.selectDispatchReportList(report));
    }

    @PreAuthorize("@ss.hasPermi('business:dispatchMonthReport:list')")
    @GetMapping("/dispatch/month/list")
    public TableDataInfo dispatchMonthList(SqDispatchReport report)
    {
        startPage();
        return getDataTable(sqReportService.selectDispatchMonthReportList(report));
    }

    @PreAuthorize("@ss.hasPermi('business:safetyEventStat:list')")
    @GetMapping("/safety-event/list")
    public TableDataInfo safetyEventList(SqSafetyEventStat stat)
    {
        startPage();
        return getDataTable(sqReportService.selectSafetyEventStatList(stat));
    }

    @PreAuthorize("@ss.hasAnyPermi('business:dispatchShiftReport:list,business:dispatchDayReport:list,business:dispatchMonthReport:list')")
    @GetMapping("/dispatch/{id}")
    public AjaxResult getDispatchInfo(@PathVariable Long id)
    {
        return success(sqReportService.selectDispatchReportById(id));
    }
}
