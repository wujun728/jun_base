package com.aidex.generator.controller;

import com.github.pagehelper.PageInfo;
import com.aidex.common.annotation.Log;
import com.aidex.common.core.controller.BaseController;
import com.aidex.common.core.domain.R;
import com.aidex.common.core.page.PageDomain;
import com.aidex.common.enums.BusinessType;
import com.aidex.generator.domain.GenConfigTemplate;
import com.aidex.generator.service.IGenConfigTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

/**
 * 模板配置Controller
 * @author ruoyi
 * @date 2021-03-06
 */
@RestController
@RequestMapping("/system/template")
public class GenConfigTemplateController extends BaseController
{
    @Autowired
    private IGenConfigTemplateService genConfigTemplateService;

    /**
     * 查询模板配置列表
     */
    @PreAuthorize("@ss.hasPermi('gen:template:list')")
    @GetMapping("/list")
    public R<PageInfo> list(GenConfigTemplate genConfigTemplate, HttpServletRequest request, HttpServletResponse response)
    {
        genConfigTemplate.setPage(new PageDomain(request, response));
        return R.data(genConfigTemplateService.findPage(genConfigTemplate));
    }


    /**
     * 获取模板配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('gen:template:query')")
    @GetMapping(value = "/{id}")
    public R<GenConfigTemplate> detail(@PathVariable("id") String id)
    {
        return R.data(genConfigTemplateService.get(id));
    }

    /**
     * 新增模板配置
     */
    @PreAuthorize("@ss.hasPermi('gen:template:add')")
    @Log(title = "模板配置", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody @Validated GenConfigTemplate genConfigTemplate)
    {
        return R.status(genConfigTemplateService.save(genConfigTemplate));
    }

    /**
     * 修改模板配置
     */
    @PreAuthorize("@ss.hasPermi('gen:template:edit')")
    @Log(title = "模板配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody @Validated GenConfigTemplate genConfigTemplate)
    {
        return R.status(genConfigTemplateService.save(genConfigTemplate));
    }

    /**
     * 删除模板配置
     */
    @PreAuthorize("@ss.hasPermi('gen:template:remove')")
    @Log(title = "模板配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids)
    {
        return R.status(genConfigTemplateService.deleteGenConfigTemplateByIds(ids));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('gen:template:changeStatus')")
    @Log(title = "模板配置", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R changeStatus(@RequestBody GenConfigTemplate genConfigTemplate)
    {
        GenConfigTemplate updateGenConfigTemplate = genConfigTemplateService.get(genConfigTemplate.getId());
        updateGenConfigTemplate.setStatus(genConfigTemplate.getStatus());
        return R.data(genConfigTemplateService.save(updateGenConfigTemplate));
    }

    /**
     * 是否默认修改
     */
    @PreAuthorize("@ss.hasPermi('gen:template:edit')")
    @Log(title = "模板配置", businessType = BusinessType.UPDATE)
    @PutMapping("/changeTemplateDefault")
    public R changeTemplateDefault(@RequestBody GenConfigTemplate genConfigTemplate)
    {
        GenConfigTemplate updateGenConfigTemplate = genConfigTemplateService.get(genConfigTemplate.getId());
        updateGenConfigTemplate.setTemplateDefault(genConfigTemplate.getTemplateDefault());
        return R.data(genConfigTemplateService.save(updateGenConfigTemplate));
    }

    /**
     * 获取当前最大编号
     * @return
     */
    @PreAuthorize("@ss.hasPermi('gen:template:edit')")
    @Log(title = "模板配置", businessType = BusinessType.SELECT)
    @GetMapping("/findMaxSort")
    public R findMaxSort() {
        return R.data(genConfigTemplateService.findMaxSort(new GenConfigTemplate()));
    }

    /**
     * 校验模板名称的唯一性
     * @return
     */
    @PreAuthorize("@ss.hasPermi('gen:template:edit')")
    @Log(title = "模板配置", businessType = BusinessType.CHECK)
    @GetMapping(value = {"/checkTemplateNameUnique/{templateName}/{id}", "/checkTemplateNameUnique/{templateName}"})
    public R checkTemplateNameUnique(@NotBlank(message = "模板名称不允许为空") @PathVariable("templateName") String templateName, @PathVariable(value = "id", required = false) String id) {
        GenConfigTemplate genConfigTemplate = new GenConfigTemplate();
        genConfigTemplate.setTemplateName(templateName);
        genConfigTemplate.setId(id);
        genConfigTemplateService.checkTemplateNameUnique(genConfigTemplate);
        return R.status(Boolean.TRUE);
    }
}