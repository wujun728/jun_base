package com.ruoyi.business.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.business.domain.SqSiren;
import com.ruoyi.business.service.ISqSirenService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/business/sq/siren")
public class SqSirenController extends BaseController
{
    @Autowired private ISqSirenService service;

    @PreAuthorize("@ss.hasPermi('business:siren:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqSiren siren) { startPage(); return getDataTable(service.selectSqSirenList(siren)); }

    @Log(title = "电子围栏管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:siren:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqSiren siren)
    {
        new ExcelUtil<SqSiren>(SqSiren.class).exportExcel(response, service.selectSqSirenList(siren), "电子围栏管理");
    }

    @PreAuthorize("@ss.hasPermi('business:siren:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) { return success(service.selectSqSirenById(id)); }

    @Log(title = "电子围栏管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:siren:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SqSiren siren) { return toAjax(service.insertSqSiren(siren)); }

    @Log(title = "电子围栏管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:siren:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody SqSiren siren) { return toAjax(service.updateSqSiren(siren)); }

    @Log(title = "电子围栏管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:siren:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) { return toAjax(service.deleteSqSirenByIds(ids)); }
}
