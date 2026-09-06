package com.ruoyi.business.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.business.domain.SqSafetyAlert;
import com.ruoyi.business.service.ISqSafetyAlertService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/business/sq/safety-alert")
public class SqSafetyAlertController extends BaseController
{
    @Autowired private ISqSafetyAlertService service;

    @PreAuthorize("@ss.hasPermi('business:safetyAlert:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqSafetyAlert alert) { startPage(); return getDataTable(service.selectSqSafetyAlertList(alert)); }

    @Log(title = "溜井作业监测", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:safetyAlert:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqSafetyAlert alert)
    {
        new ExcelUtil<SqSafetyAlert>(SqSafetyAlert.class).exportExcel(response, service.selectSqSafetyAlertList(alert), "溜井作业监测");
    }

    @PreAuthorize("@ss.hasPermi('business:safetyAlert:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) { return success(service.selectSqSafetyAlertById(id)); }

    @Log(title = "溜井作业监测", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:safetyAlert:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SqSafetyAlert alert) { alert.setCreateUser(getUsername()); return toAjax(service.insertSqSafetyAlert(alert)); }

    @Log(title = "溜井作业监测", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:safetyAlert:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody SqSafetyAlert alert) { alert.setUpdateUser(getUsername()); return toAjax(service.updateSqSafetyAlert(alert)); }

    @Log(title = "溜井作业监测", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:safetyAlert:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) { return toAjax(service.deleteSqSafetyAlertByIds(ids)); }
}
