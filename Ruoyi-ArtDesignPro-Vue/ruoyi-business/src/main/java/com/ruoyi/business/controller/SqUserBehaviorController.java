package com.ruoyi.business.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.business.domain.SqBehaviorMonitor;
import com.ruoyi.business.service.ISqUserBehaviorService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/business/sq/user-behavior")
public class SqUserBehaviorController extends BaseController
{
    @Autowired private ISqUserBehaviorService service;

    @PreAuthorize("@ss.hasPermi('business:userBehavior:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqBehaviorMonitor behavior) { startPage(); return getDataTable(service.selectSqUserBehaviorList(behavior)); }

    @Log(title = "人员行为监测", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:userBehavior:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqBehaviorMonitor behavior)
    {
        new ExcelUtil<SqBehaviorMonitor>(SqBehaviorMonitor.class).exportExcel(response, service.selectSqUserBehaviorList(behavior), "人员行为监测");
    }

    @PreAuthorize("@ss.hasPermi('business:userBehavior:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) { return success(service.selectSqUserBehaviorById(id)); }

    @Log(title = "人员行为监测", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:userBehavior:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SqBehaviorMonitor behavior) { return toAjax(service.insertSqUserBehavior(behavior)); }

    @Log(title = "人员行为监测", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:userBehavior:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody SqBehaviorMonitor behavior) { return toAjax(service.updateSqUserBehavior(behavior)); }

    @Log(title = "人员行为监测", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:userBehavior:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) { return toAjax(service.deleteSqUserBehaviorByIds(ids)); }
}
