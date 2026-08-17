package com.ruoyi.datarule.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.datarule.domain.SysDataRule;
import com.ruoyi.datarule.resolver.DataRuleVariableResolver;
import com.ruoyi.datarule.service.ISysDataRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/dataRule")
public class SysDataRuleController extends BaseController {

    @Autowired
    private ISysDataRuleService sysDataRuleService;

    @PreAuthorize("@ss.hasPermi('system:dataRule:list')")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam Map<String, Object> params) {
        QueryWrapper<SysDataRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        String modelName = (String) params.get("modelName");
        if (StringUtils.isNotEmpty(modelName)) {
            queryWrapper.like("model_name", modelName);
        }
        Page<SysDataRule> page = sysDataRuleService.page(getPage(), queryWrapper);
        return getDataTable(page.getRecords());
    }

    @GetMapping("/models")
    public AjaxResult models() {
        return AjaxResult.success(sysDataRuleService.getModelList());
    }

    @GetMapping("/fields")
    public AjaxResult fields(@RequestParam String tableName) {
        return AjaxResult.success(sysDataRuleService.getFieldList(tableName));
    }

    @GetMapping("/variables")
    public AjaxResult variables() {
        return AjaxResult.success(DataRuleVariableResolver.getBuiltInVariables());
    }

    @GetMapping("/roles")
    public AjaxResult roles() {
        return AjaxResult.success(sysDataRuleService.getRoleList());
    }

    @PreAuthorize("@ss.hasPermi('system:dataRule:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysDataRuleService.getById(id));
    }

    @PreAuthorize("@ss.hasPermi('system:dataRule:add')")
    @Log(title = "数据规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysDataRule sysDataRule) {
        return toAjax(sysDataRuleService.save(sysDataRule));
    }

    @PreAuthorize("@ss.hasPermi('system:dataRule:edit')")
    @Log(title = "数据规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysDataRule sysDataRule) {
        return toAjax(sysDataRuleService.updateById(sysDataRule));
    }

    @PreAuthorize("@ss.hasPermi('system:dataRule:remove')")
    @Log(title = "数据规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return toAjax(sysDataRuleService.removeByIds(Arrays.asList(infoIds)));
    }
}