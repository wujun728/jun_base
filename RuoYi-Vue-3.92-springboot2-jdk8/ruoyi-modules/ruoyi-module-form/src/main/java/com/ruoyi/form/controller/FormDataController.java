package com.ruoyi.form.controller;

import java.util.List;

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

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.form.domain.FormData;
import com.ruoyi.form.service.IFormDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 单数据Controller
 * 
 * @author ruoyi
 * @date 2025-05-12
 */
@RestController
@RequestMapping("/form/data")
@Tag(name = "【单数据】管理")
public class FormDataController extends BaseController
{
    @Autowired
    private IFormDataService formDataService;

    /**
     * 查询单数据列表
     */
    @Operation(summary = "查询单数据列表")
    @PreAuthorize("@ss.hasPermi('form:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(FormData formData)
    {
        startPage();
        List<FormData> list = formDataService.selectFormDataList(formData);
        return getDataTable(list);
    }

    /**
     * 导出单数据列表
     */
    @Operation(summary = "导出单数据列表")
    @PreAuthorize("@ss.hasPermi('form:data:export')")
    @Log(title = "单数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FormData formData)
    {
        List<FormData> list = formDataService.selectFormDataList(formData);
        ExcelUtil<FormData> util = new ExcelUtil<FormData>(FormData.class);
        util.exportExcel(response, list, "单数据数据");
    }

    /**
     * 获取单数据详细信息
     */
    @Operation(summary = "获取单数据详细信息")
    @PreAuthorize("@ss.hasPermi('form:data:query')")
    @GetMapping(value = "/{dataId}")
    public AjaxResult getInfo(@PathVariable("dataId") Long dataId)
    {
        return success(formDataService.selectFormDataByDataId(dataId));
    }

    /**
     * 新增单数据
     */
    @Operation(summary = "新增单数据")
    @PreAuthorize("@ss.hasPermi('form:data:add')")
    @Log(title = "单数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FormData formData)
    {
        formData.setCreateBy(getUsername());
        return toAjax(formDataService.insertFormData(formData));
    }

    /**
     * 修改单数据
     */
    @Operation(summary = "修改单数据")
    @PreAuthorize("@ss.hasPermi('form:data:edit')")
    @Log(title = "单数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FormData formData)
    {
        formData.setUpdateBy(getUsername());
        return toAjax(formDataService.updateFormData(formData));
    }

    /**
     * 删除单数据
     */
    @Operation(summary = "删除单数据")
    @PreAuthorize("@ss.hasPermi('form:data:remove')")
    @Log(title = "单数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dataIds}")
    public AjaxResult remove(@PathVariable( name = "dataIds" ) Long[] dataIds) 
    {
        return toAjax(formDataService.deleteFormDataByDataIds(dataIds));
    }
}
