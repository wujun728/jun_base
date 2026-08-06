package com.ruoyi.modelMessage.controller;

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
import com.ruoyi.modelMessage.domain.MessageTemplate;
import com.ruoyi.modelMessage.service.IMessageTemplateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 模版管理Controller
 * 
 * @author ruoyi
 * @date 2024-12-31
 */
@RestController
@RequestMapping("/modelMessage/template")
@Tag(name = "【模版管理】管理")
public class MessageTemplateController extends BaseController
{
    @Autowired
    private IMessageTemplateService messageTemplateService;

    /**
     * 查询模版管理列表
     */
    @Operation(summary = "查询模版管理列表")
    //@PreAuthorize("@ss.hasPermi('modelMessage:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(MessageTemplate messageTemplate)
    {
        startPage();
        List<MessageTemplate> list = messageTemplateService.selectMessageTemplateList(messageTemplate);
        return getDataTable(list);
    }

    /**
     * 导出模版管理列表
     */
    @Operation(summary = "导出模版管理列表")
    @PreAuthorize("@ss.hasPermi('modelMessage:template:export')")
    @Log(title = "模版管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MessageTemplate messageTemplate)
    {
        List<MessageTemplate> list = messageTemplateService.selectMessageTemplateList(messageTemplate);
        ExcelUtil<MessageTemplate> util = new ExcelUtil<MessageTemplate>(MessageTemplate.class);
        util.exportExcel(response, list, "模版管理数据");
    }

    /**
     * 获取模版管理详细信息
     */
    @Operation(summary = "获取模版管理详细信息")
    //@PreAuthorize("@ss.hasPermi('modelMessage:template:query')")
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable("templateId") Long templateId)
    {
        return success(messageTemplateService.selectMessageTemplateByTemplateId(templateId));
    }

    /**
     * 新增模版管理
     */
    @Operation(summary = "新增模版管理")
    //@PreAuthorize("@ss.hasPermi('modelMessage:template:add')")
    @Log(title = "模版管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MessageTemplate messageTemplate)
    {
        return toAjax(messageTemplateService.insertMessageTemplate(messageTemplate));
    }

    /**
     * 修改模版管理
     */
    @Operation(summary = "修改模版管理")
    //@PreAuthorize("@ss.hasPermi('modelMessage:template:edit')")
    @Log(title = "模版管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MessageTemplate messageTemplate)
    {
        return toAjax(messageTemplateService.updateMessageTemplate(messageTemplate));
    }

    /**
     * 删除模版管理
     */
    @Operation(summary = "删除模版管理")
    //@PreAuthorize("@ss.hasPermi('modelMessage:template:remove')")
    @Log(title = "模版管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable( name = "templateIds" ) Long[] templateIds) 
    {
        return toAjax(messageTemplateService.deleteMessageTemplateByTemplateIds(templateIds));
    }

     /**
     * 查询模版签名
     * @return 模版信息列表
     */
    @Operation(summary = "查询模版下拉框")
    @GetMapping("/selecTemplates")
    public AjaxResult selecTemplates() {
        return success(messageTemplateService.selecTemplates());
    }
}
