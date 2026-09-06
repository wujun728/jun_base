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
import com.ruoyi.business.domain.SqLevelLog;
import com.ruoyi.business.service.ISqLevelLogService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/business/sq/level-log")
public class SqLevelLogController extends BaseController
{
    @Autowired
    private ISqLevelLogService sqLevelLogService;

    @PreAuthorize("@ss.hasPermi('business:levelLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqLevelLog log)
    {
        startPage();
        return getDataTable(sqLevelLogService.selectSqLevelLogList(log));
    }

    @Log(title = "料位一览表", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:levelLog:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqLevelLog log)
    {
        List<SqLevelLog> list = sqLevelLogService.selectSqLevelLogList(log);
        new ExcelUtil<SqLevelLog>(SqLevelLog.class).exportExcel(response, list, "料位一览表");
    }

    @PreAuthorize("@ss.hasPermi('business:levelLog:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(sqLevelLogService.selectSqLevelLogById(id));
    }

    @Log(title = "料位一览表", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:levelLog:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SqLevelLog log)
    {
        return toAjax(sqLevelLogService.insertSqLevelLog(log));
    }

    @Log(title = "料位一览表", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:levelLog:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody SqLevelLog log)
    {
        return toAjax(sqLevelLogService.updateSqLevelLog(log));
    }

    @Log(title = "料位一览表", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:levelLog:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sqLevelLogService.deleteSqLevelLogByIds(ids));
    }
}
