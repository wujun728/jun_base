package com.ruoyi.datarule.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDataRule;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.datarule.handler.DataRuleHandler;
import com.ruoyi.datarule.domain.SysRoleDataRule;
import com.ruoyi.datarule.service.ISysDataRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/dataRule")
public class SysDataRuleController extends BaseController {
    @Autowired
    private ISysDataRuleService sysDataRuleService;

    //所有数据更新都需要刷新数据权限
    @Autowired
    private DataRuleHandler dataRuleHandler;

    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:dataRule:list')")
    @GetMapping("/list")
    public TableDataInfo list(HttpServletRequest request) {
        QueryWrapper<SysDataRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        String tenantId = request.getParameter("tenantId");
        if (!StringUtils.isEmpty(tenantId)) {
            queryWrapper.eq("tenant_id", tenantId);
        }
        String contact = request.getParameter("contact");
        if (!StringUtils.isEmpty(contact)) {
            queryWrapper.like("contact", contact);
        }
        String phone = request.getParameter("phone");
        if (!StringUtils.isEmpty(phone)) {
            queryWrapper.eq("phone", phone);
        }
        String name = request.getParameter("name");
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }

        Page<SysDataRule> page = sysDataRuleService.page(getPage(), queryWrapper);
        return getDataTable(page.getRecords());
    }

    @GetMapping(value = "roleDataRuleList")
    @PreAuthorize("@ss.hasPermi('system:dataRule:list')")
    public TableDataInfo roleDataRuleList(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<SysRoleDataRule> roleDataRuleEntityWrapper = new QueryWrapper<>();
        String roleId = request.getParameter("roleId");
        roleDataRuleEntityWrapper.eq("role_id", roleId);
        // 预处理
        List<SysRoleDataRule> list = sysDataRuleService.roleDataList(roleDataRuleEntityWrapper);

        QueryWrapper<SysDataRule> entityWrapper = new QueryWrapper<>();
        entityWrapper.orderByDesc( "create_time");
        String resourceCode = request.getParameter("resourceCode");
        if (!StringUtils.isEmpty(resourceCode)) {
            entityWrapper.eq("resource_code", resourceCode);
        }
        String scopeName = request.getParameter("scopeName");
        if (!StringUtils.isEmpty(scopeName)) {
            entityWrapper.eq("scope_name", scopeName);
        }
        // 预处理
        Page<SysDataRule> pageBean = sysDataRuleService.page(getPage(), entityWrapper);
        pageBean.getRecords().forEach(item -> {
            list.forEach(roleDataRule -> {
                if (item.getId().equals(roleDataRule.getScopeId())) {
                    item.setSelect(true);
                }
            });
        });
        return getDataTable(pageBean.getRecords());
    }
    @PostMapping("updateRules")
    @Log(title = "数据规则", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:dataRule:update')")
    public AjaxResult update(@RequestBody SysRoleDataRule sysRoleDataRule) {
        QueryWrapper<SysRoleDataRule> roleDataRuleEntityWrapper = new QueryWrapper<>();
        roleDataRuleEntityWrapper.eq("role_id", sysRoleDataRule.getRoleId());
        sysDataRuleService.removeRoleDataRule(roleDataRuleEntityWrapper);
        if (StringUtils.isEmpty(sysRoleDataRule.getIds())) {
            dataRuleHandler.refreshRole();
            return AjaxResult.success("更新成功");
        }
        String[] ids = sysRoleDataRule.getIds().split(",");
        List<String> idList = java.util.Arrays.asList(ids);
        List<SysRoleDataRule> roleDataRuleList = new ArrayList<>();
        idList.forEach(item -> {
            SysRoleDataRule roleDataRule = new SysRoleDataRule();
            roleDataRule.setRoleId(sysRoleDataRule.getRoleId());
            roleDataRule.setScopeId(item);
            roleDataRuleList.add(roleDataRule);
        });
        sysDataRuleService.insertBatchRoleDataRule(roleDataRuleList);
        dataRuleHandler.refreshRole();
        return AjaxResult.success("更新成功");
    }

    /**
     * 导出数据规则列表
     */
    @PreAuthorize("@ss.hasPermi('system:dataRule:export')")
    @Log(title = "数据规则", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(HttpServletRequest request) {
        QueryWrapper<SysDataRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("a.create_time");
        String tenantId = request.getParameter("tenantId");
        if (!StringUtils.isEmpty(tenantId)) {
            queryWrapper.eq("a.tenant_id", tenantId);
        }
        String contact = request.getParameter("contact");
        if (!StringUtils.isEmpty(contact)) {
            queryWrapper.like("contact", contact);
        }
        String phone = request.getParameter("phone");
        if (!StringUtils.isEmpty(phone)) {
            queryWrapper.eq("phone", phone);
        }
        String name = request.getParameter("name");
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }

        List<SysDataRule> list = sysDataRuleService.list(queryWrapper);
        ExcelUtil<SysDataRule> util = new ExcelUtil<>(SysDataRule.class);
        return util.exportExcel(list, "数据规则数据");
    }

    /**
     * 获取数据规则详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dataRule:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysDataRuleService.getById(id));
    }

    /**
     * 新增数据规则
     */
    @PreAuthorize("@ss.hasPermi('system:dataRule:add')")
    @Log(title = "数据规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysDataRule sysDataRule) {
        return toAjax(sysDataRuleService.save(sysDataRule));
    }

    /**
     * 修改数据规则
     */
    @PreAuthorize("@ss.hasPermi('system:dataRule:edit')")
    @Log(title = "数据规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysDataRule sysDataRule) {
        return toAjax(sysDataRuleService.updateById(sysDataRule));
    }

    /**
     * 删除数据规则
     */
    @PreAuthorize("@ss.hasPermi('system:dataRule:remove')")
    @Log(title = "数据规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return toAjax(sysDataRuleService.removeByIds(Arrays.asList(infoIds)));
    }
}
