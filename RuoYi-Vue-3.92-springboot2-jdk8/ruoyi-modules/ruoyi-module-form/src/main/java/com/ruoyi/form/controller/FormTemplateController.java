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
import com.ruoyi.form.domain.FormTemplate;
import com.ruoyi.form.service.IFormTemplateService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletResponse;

/**
 * 单模板Controller
 * 
 * @author ruoyi
 * @date 2025-05-12
 */
@RestController
@RequestMapping("/form/template")
@Api(tags = "【单模板】管理")
public class FormTemplateController extends BaseController
{
    @Autowired
    private IFormTemplateService formTemplateService;

    /**
     * 查询单模板列表
     */
    @ApiOperation("查询单模板列表")
    @PreAuthorize("@ss.hasPermi('form:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(FormTemplate formTemplate)
    {
        startPage();
        List<FormTemplate> list = formTemplateService.selectFormTemplateList(formTemplate);
        return getDataTable(list);
    }

    /**
     * 导出单模板列表
     */
    @ApiOperation("导出单模板列表")
    @PreAuthorize("@ss.hasPermi('form:template:export')")
    @Log(title = "单模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FormTemplate formTemplate)
    {
        List<FormTemplate> list = formTemplateService.selectFormTemplateList(formTemplate);
        ExcelUtil<FormTemplate> util = new ExcelUtil<FormTemplate>(FormTemplate.class);
        util.exportExcel(response, list, "单模板数据");
    }

    /**
     * 获取单模板详细信息
     */
    @ApiOperation("获取单模板详细信息")
    @PreAuthorize("@ss.hasPermi('form:template:query')")
    @GetMapping(value = "/{formId}")
    public AjaxResult getInfo(@PathVariable("formId") Long formId)
    {
        return success(formTemplateService.selectFormTemplateByFormId(formId));
    }

    /**
     * 新增单模板
     */
    @ApiOperation("新增单模板")
    @PreAuthorize("@ss.hasPermi('form:template:add')")
    @Log(title = "单模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FormTemplate formTemplate)
    {
        return toAjax(formTemplateService.insertFormTemplate(formTemplate));
    }

    /**
     * 修改单模板
     */
    @ApiOperation("修改单模板")
    @PreAuthorize("@ss.hasPermi('form:template:edit')")
    @Log(title = "单模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FormTemplate formTemplate)
    {
        return toAjax(formTemplateService.updateFormTemplate(formTemplate));
    }

    /**
     * 删除单模板
     */
    @ApiOperation("删除单模板")
    @PreAuthorize("@ss.hasPermi('form:template:remove')")
    @Log(title = "单模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{formIds}")
    public AjaxResult remove(@PathVariable( name = "formIds" ) Long[] formIds) 
    {
        return toAjax(formTemplateService.deleteFormTemplateByFormIds(formIds));
    }
}
