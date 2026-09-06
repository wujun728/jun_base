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
import com.ruoyi.business.domain.SqWxDept;
import com.ruoyi.business.service.ISqWxDeptService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/business/sq/wx-dept")
public class SqWxDeptController extends BaseController
{
    @Autowired
    private ISqWxDeptService sqWxDeptService;

    @PreAuthorize("@ss.hasPermi('business:wxOrg:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqWxDept dept)
    {
        startPage();
        return getDataTable(sqWxDeptService.selectSqWxDeptList(dept));
    }

    @PreAuthorize("@ss.hasPermi('business:wxOrg:list')")
    @GetMapping("/options")
    public AjaxResult options(SqWxDept dept)
    {
        return success(sqWxDeptService.selectSqWxDeptList(dept));
    }

    @Log(title = "微信组织管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:wxOrg:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqWxDept dept)
    {
        List<SqWxDept> list = sqWxDeptService.selectSqWxDeptList(dept);
        new ExcelUtil<SqWxDept>(SqWxDept.class).exportExcel(response, list, "微信组织管理");
    }

    @PreAuthorize("@ss.hasPermi('business:wxOrg:query')")
    @GetMapping("/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId)
    {
        return success(sqWxDeptService.selectSqWxDeptById(deptId));
    }

    @Log(title = "微信组织管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:wxOrg:add')")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SqWxDept dept)
    {
        dept.setCreateBy(getUsername());
        return toAjax(sqWxDeptService.insertSqWxDept(dept));
    }

    @Log(title = "微信组织管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:wxOrg:edit')")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SqWxDept dept)
    {
        dept.setUpdateBy(getUsername());
        return toAjax(sqWxDeptService.updateSqWxDept(dept));
    }

    @Log(title = "微信组织管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:wxOrg:remove')")
    @DeleteMapping("/{deptIds}")
    public AjaxResult remove(@PathVariable Long[] deptIds)
    {
        return toAjax(sqWxDeptService.deleteSqWxDeptByIds(deptIds));
    }
}
