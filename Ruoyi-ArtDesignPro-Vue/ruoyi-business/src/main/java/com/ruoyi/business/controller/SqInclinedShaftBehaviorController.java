package com.ruoyi.business.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.business.domain.SqBehaviorMonitor;
import com.ruoyi.business.service.ISqInclinedShaftBehaviorService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/business/sq/inclined-shaft")
public class SqInclinedShaftBehaviorController extends BaseController
{
    @Autowired private ISqInclinedShaftBehaviorService service;

    @PreAuthorize("@ss.hasPermi('business:inclinedShaft:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqBehaviorMonitor behavior) { startPage(); return getDataTable(service.selectSqInclinedShaftBehaviorList(behavior)); }

    @Log(title = "斜井作业监测", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:inclinedShaft:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqBehaviorMonitor behavior)
    {
        new ExcelUtil<SqBehaviorMonitor>(SqBehaviorMonitor.class).exportExcel(response, service.selectSqInclinedShaftBehaviorList(behavior), "斜井作业监测");
    }

    @PreAuthorize("@ss.hasPermi('business:inclinedShaft:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) { return success(service.selectSqInclinedShaftBehaviorById(id)); }

    @Log(title = "斜井作业监测", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:inclinedShaft:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SqBehaviorMonitor behavior) { return toAjax(service.insertSqInclinedShaftBehavior(behavior)); }

    @Log(title = "斜井作业监测", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:inclinedShaft:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody SqBehaviorMonitor behavior) { return toAjax(service.updateSqInclinedShaftBehavior(behavior)); }

    @Log(title = "斜井作业监测", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:inclinedShaft:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) { return toAjax(service.deleteSqInclinedShaftBehaviorByIds(ids)); }
}
