package com.ruoyi.business.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.business.domain.SqChute;
import com.ruoyi.business.service.ISqChuteService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/business/sq/device-camera")
public class SqDeviceCameraController extends BaseController
{
    @Autowired
    private ISqChuteService sqChuteService;

    @PreAuthorize("@ss.hasPermi('business:deviceCamera:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqChute chute)
    {
        startPage();
        return getDataTable(sqChuteService.selectSqChuteList(chute));
    }

    @Log(title = "监控设备管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:deviceCamera:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqChute chute)
    {
        List<SqChute> list = sqChuteService.selectSqChuteList(chute);
        new ExcelUtil<SqChute>(SqChute.class).exportExcel(response, list, "监控设备管理");
    }

    @PreAuthorize("@ss.hasPermi('business:deviceCamera:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(sqChuteService.selectSqChuteById(id));
    }

    @Log(title = "监控设备管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:deviceCamera:add')")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SqChute chute)
    {
        chute.setCreateUser(getUsername());
        return toAjax(sqChuteService.insertSqChute(chute));
    }

    @Log(title = "监控设备管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:deviceCamera:edit')")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SqChute chute)
    {
        chute.setUpdateUser(getUsername());
        return toAjax(sqChuteService.updateSqChute(chute));
    }

    @Log(title = "监控设备管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:deviceCamera:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sqChuteService.deleteSqChuteByIds(ids));
    }
}
