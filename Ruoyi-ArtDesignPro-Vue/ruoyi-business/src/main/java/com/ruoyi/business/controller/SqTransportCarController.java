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
import com.ruoyi.business.domain.SqTransportCar;
import com.ruoyi.business.service.ISqTransportCarService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "旧系统SQ车辆管理")
@RestController
@RequestMapping("/business/sq/transport-car")
public class SqTransportCarController extends BaseController
{
    @Autowired
    private ISqTransportCarService sqTransportCarService;

    @PreAuthorize("@ss.hasPermi('business:transportCar:list')")
    @Operation(summary = "查询车辆管理列表")
    @GetMapping("/list")
    public TableDataInfo list(SqTransportCar car)
    {
        startPage();
        return getDataTable(sqTransportCarService.selectSqTransportCarList(car));
    }

    @Log(title = "车辆管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:transportCar:export')")
    @Operation(summary = "导出车辆管理")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqTransportCar car)
    {
        List<SqTransportCar> list = sqTransportCarService.selectSqTransportCarList(car);
        new ExcelUtil<SqTransportCar>(SqTransportCar.class).exportExcel(response, list, "车辆管理");
    }

    @PreAuthorize("@ss.hasPermi('business:transportCar:query')")
    @Operation(summary = "获取车辆管理")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(sqTransportCarService.selectSqTransportCarById(id));
    }

    @Log(title = "车辆管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:transportCar:add')")
    @Operation(summary = "新增车辆管理")
    @PostMapping
    public AjaxResult add(@RequestBody SqTransportCar car)
    {
        car.setCreateUser(getUsername());
        return toAjax(sqTransportCarService.insertSqTransportCar(car));
    }

    @Log(title = "车辆管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:transportCar:edit')")
    @Operation(summary = "修改车辆管理")
    @PutMapping
    public AjaxResult edit(@RequestBody SqTransportCar car)
    {
        car.setUpdateUser(getUsername());
        return toAjax(sqTransportCarService.updateSqTransportCar(car));
    }

    @Log(title = "车辆管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:transportCar:remove')")
    @Operation(summary = "删除车辆管理")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sqTransportCarService.deleteSqTransportCarByIds(ids));
    }
}
