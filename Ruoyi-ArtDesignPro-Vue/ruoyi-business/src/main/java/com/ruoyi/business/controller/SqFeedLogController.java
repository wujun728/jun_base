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
import com.ruoyi.business.service.ISqFeedLogService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "旧系统SQ入料口记录")
@RestController
@RequestMapping("/business/sq/feed-log")
public class SqFeedLogController extends BaseController
{
    @Autowired
    private ISqFeedLogService sqFeedLogService;

    @PreAuthorize("@ss.hasPermi('business:feedLog:list')")
    @Operation(summary = "查询入料口记录列表")
    @GetMapping("/list")
    public TableDataInfo list(SqFeedLog log)
    {
        startPage();
        List<SqFeedLog> list = sqFeedLogService.selectSqFeedLogList(log);
        return getDataTable(list);
    }

    @Log(title = "入料口记录", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:feedLog:export')")
    @Operation(summary = "导出入料口记录")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqFeedLog log)
    {
        List<SqFeedLog> list = sqFeedLogService.selectSqFeedLogList(log);
        new ExcelUtil<SqFeedLog>(SqFeedLog.class).exportExcel(response, list, "入料口记录");
    }

    @PreAuthorize("@ss.hasPermi('business:feedLog:query')")
    @Operation(summary = "获取入料口记录")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(sqFeedLogService.selectSqFeedLogById(id));
    }

    @Log(title = "入料口记录", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:feedLog:add')")
    @Operation(summary = "新增入料口记录")
    @PostMapping
    public AjaxResult add(@RequestBody SqFeedLog log)
    {
        log.setCreateUser(getUsername());
        return toAjax(sqFeedLogService.insertSqFeedLog(log));
    }

    @Log(title = "入料口记录", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:feedLog:edit')")
    @Operation(summary = "修改入料口记录")
    @PutMapping
    public AjaxResult edit(@RequestBody SqFeedLog log)
    {
        log.setUpdateUser(getUsername());
        return toAjax(sqFeedLogService.updateSqFeedLog(log));
    }

    @Log(title = "入料口记录", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:feedLog:remove')")
    @Operation(summary = "删除入料口记录")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sqFeedLogService.deleteSqFeedLogByIds(ids));
    }
}
