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
import com.ruoyi.business.domain.SqOutLog;
import com.ruoyi.business.service.ISqOutLogService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "旧系统SQ出料口记录")
@RestController
@RequestMapping("/business/sq/out-log")
public class SqOutLogController extends BaseController
{
    @Autowired
    private ISqOutLogService sqOutLogService;

    @PreAuthorize("@ss.hasPermi('business:outLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqOutLog log)
    {
        startPage();
        return getDataTable(sqOutLogService.selectSqOutLogList(log));
    }

    @Log(title = "出料口记录", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:outLog:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqOutLog log)
    {
        new ExcelUtil<SqOutLog>(SqOutLog.class).exportExcel(response, sqOutLogService.selectSqOutLogList(log), "出料口记录");
    }

    @PreAuthorize("@ss.hasPermi('business:outLog:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(sqOutLogService.selectSqOutLogById(id));
    }

    @Log(title = "出料口记录", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:outLog:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SqOutLog log)
    {
        return toAjax(sqOutLogService.insertSqOutLog(log));
    }

    @Log(title = "出料口记录", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:outLog:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody SqOutLog log)
    {
        return toAjax(sqOutLogService.updateSqOutLog(log));
    }

    @Log(title = "出料口记录", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:outLog:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sqOutLogService.deleteSqOutLogByIds(ids));
    }
}
