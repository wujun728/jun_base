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
import com.ruoyi.business.domain.BusinessDemo;
import com.ruoyi.business.service.IBusinessDemoService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "业务示例")
@RestController
@RequestMapping("/business/demo")
public class BusinessDemoController extends BaseController
{
    @Autowired
    private IBusinessDemoService businessDemoService;

    @PreAuthorize("@ss.hasPermi('business:demo:list')")
    @Operation(summary = "查询业务示例列表")
    @GetMapping("/list")
    public TableDataInfo list(BusinessDemo demo)
    {
        startPage();
        List<BusinessDemo> list = businessDemoService.selectBusinessDemoList(demo);
        return getDataTable(list);
    }

    @Log(title = "业务示例", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:demo:export')")
    @Operation(summary = "导出业务示例")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusinessDemo demo)
    {
        List<BusinessDemo> list = businessDemoService.selectBusinessDemoList(demo);
        ExcelUtil<BusinessDemo> util = new ExcelUtil<BusinessDemo>(BusinessDemo.class);
        util.exportExcel(response, list, "业务示例数据");
    }

    @PreAuthorize("@ss.hasPermi('business:demo:query')")
    @Operation(summary = "获取业务示例详情")
    @GetMapping("/{demoId}")
    public AjaxResult getInfo(@PathVariable Long demoId)
    {
        return success(businessDemoService.selectBusinessDemoById(demoId));
    }

    @PreAuthorize("@ss.hasPermi('business:demo:add')")
    @Log(title = "业务示例", businessType = BusinessType.INSERT)
    @Operation(summary = "新增业务示例")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BusinessDemo demo)
    {
        demo.setCreateBy(getUsername());
        if (demo.getStatus() == null || demo.getStatus().isEmpty())
        {
            demo.setStatus("0");
        }
        return toAjax(businessDemoService.insertBusinessDemo(demo));
    }

    @PreAuthorize("@ss.hasPermi('business:demo:edit')")
    @Log(title = "业务示例", businessType = BusinessType.UPDATE)
    @Operation(summary = "修改业务示例")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BusinessDemo demo)
    {
        demo.setUpdateBy(getUsername());
        return toAjax(businessDemoService.updateBusinessDemo(demo));
    }

    @PreAuthorize("@ss.hasPermi('business:demo:remove')")
    @Log(title = "业务示例", businessType = BusinessType.DELETE)
    @Operation(summary = "删除业务示例")
    @DeleteMapping("/{demoIds}")
    public AjaxResult remove(@PathVariable Long[] demoIds)
    {
        return toAjax(businessDemoService.deleteBusinessDemoByIds(demoIds));
    }
}
