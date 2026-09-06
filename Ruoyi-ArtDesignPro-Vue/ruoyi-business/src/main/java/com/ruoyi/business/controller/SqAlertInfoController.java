package com.ruoyi.business.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.business.domain.SqAlertInfo;
import com.ruoyi.business.service.ISqAlertInfoService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/business/sq/alert-info")
public class SqAlertInfoController extends BaseController
{
    @Autowired private ISqAlertInfoService service;

    @PreAuthorize("@ss.hasPermi('business:alertInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqAlertInfo info) { startPage(); return getDataTable(service.selectSqAlertInfoList(info)); }

    @Log(title = "报警类别管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:alertInfo:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqAlertInfo info)
    {
        List<SqAlertInfo> list = service.selectSqAlertInfoList(info);
        new ExcelUtil<SqAlertInfo>(SqAlertInfo.class).exportExcel(response, list, "报警类别管理");
    }

    @PreAuthorize("@ss.hasPermi('business:alertInfo:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) { return success(service.selectSqAlertInfoById(id)); }

    @Log(title = "报警类别管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:alertInfo:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SqAlertInfo info) { info.setCreateUser(getUsername()); return toAjax(service.insertSqAlertInfo(info)); }

    @Log(title = "报警类别管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:alertInfo:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody SqAlertInfo info) { info.setUpdateUser(getUsername()); return toAjax(service.updateSqAlertInfo(info)); }

    @Log(title = "报警类别管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:alertInfo:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) { return toAjax(service.deleteSqAlertInfoByIds(ids)); }
}
