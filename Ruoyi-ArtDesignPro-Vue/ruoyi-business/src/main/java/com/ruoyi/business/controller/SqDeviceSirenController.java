package com.ruoyi.business.controller;

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
import com.ruoyi.business.domain.SqSiren;
import com.ruoyi.business.service.ISqSirenService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/business/sq/device-siren")
public class SqDeviceSirenController extends BaseController
{
    @Autowired
    private ISqSirenService sqSirenService;

    @PreAuthorize("@ss.hasPermi('business:deviceSiren:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqSiren siren)
    {
        startPage();
        return getDataTable(sqSirenService.selectSqSirenList(siren));
    }

    @Log(title = "声光报警器管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:deviceSiren:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqSiren siren)
    {
        new ExcelUtil<SqSiren>(SqSiren.class).exportExcel(response, sqSirenService.selectSqSirenList(siren), "声光报警器管理");
    }

    @PreAuthorize("@ss.hasPermi('business:deviceSiren:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(sqSirenService.selectSqSirenById(id));
    }

    @Log(title = "声光报警器管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:deviceSiren:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SqSiren siren)
    {
        return toAjax(sqSirenService.insertSqSiren(siren));
    }

    @Log(title = "声光报警器管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:deviceSiren:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody SqSiren siren)
    {
        return toAjax(sqSirenService.updateSqSiren(siren));
    }

    @Log(title = "声光报警器管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:deviceSiren:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sqSirenService.deleteSqSirenByIds(ids));
    }
}
