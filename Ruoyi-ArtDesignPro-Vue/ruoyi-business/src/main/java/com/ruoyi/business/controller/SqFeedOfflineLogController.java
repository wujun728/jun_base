package com.ruoyi.business.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.business.domain.SqFeedLog;
import com.ruoyi.business.service.ISqFeedOfflineLogService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/business/sq/feed-offline-log")
public class SqFeedOfflineLogController extends BaseController
{
    @Autowired
    private ISqFeedOfflineLogService sqFeedOfflineLogService;

    @PreAuthorize("@ss.hasPermi('business:feedOfflineLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqFeedLog log)
    {
        startPage();
        return getDataTable(sqFeedOfflineLogService.selectSqFeedOfflineLogList(log));
    }

    @Log(title = "入料离线记录", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:feedOfflineLog:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqFeedLog log)
    {
        List<SqFeedLog> list = sqFeedOfflineLogService.selectSqFeedOfflineLogList(log);
        new ExcelUtil<SqFeedLog>(SqFeedLog.class).exportExcel(response, list, "入料离线记录");
    }

    @PreAuthorize("@ss.hasPermi('business:feedOfflineLog:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(sqFeedOfflineLogService.selectSqFeedOfflineLogById(id));
    }

    @Log(title = "入料离线记录", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:feedOfflineLog:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SqFeedLog log)
    {
        log.setCreateUser(getUsername());
        return toAjax(sqFeedOfflineLogService.insertSqFeedOfflineLog(log));
    }

    @Log(title = "入料离线记录", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:feedOfflineLog:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody SqFeedLog log)
    {
        log.setUpdateUser(getUsername());
        return toAjax(sqFeedOfflineLogService.updateSqFeedOfflineLog(log));
    }

    @Log(title = "入料离线记录", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:feedOfflineLog:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sqFeedOfflineLogService.deleteSqFeedOfflineLogByIds(ids));
    }
}
