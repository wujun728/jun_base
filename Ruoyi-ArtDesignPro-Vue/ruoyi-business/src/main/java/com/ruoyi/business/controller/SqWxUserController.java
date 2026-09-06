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
import com.ruoyi.business.domain.SqWxUser;
import com.ruoyi.business.service.ISqWxUserService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/business/sq/wx-user")
public class SqWxUserController extends BaseController
{
    @Autowired
    private ISqWxUserService sqWxUserService;

    @PreAuthorize("@ss.hasPermi('business:wxOrg:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqWxUser user)
    {
        startPage();
        return getDataTable(sqWxUserService.selectSqWxUserList(user));
    }

    @Log(title = "企业微信用户", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:wxOrg:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqWxUser user)
    {
        List<SqWxUser> list = sqWxUserService.selectSqWxUserList(user);
        new ExcelUtil<SqWxUser>(SqWxUser.class).exportExcel(response, list, "企业微信用户");
    }

    @PreAuthorize("@ss.hasPermi('business:wxOrg:query')")
    @GetMapping("/{userId}")
    public AjaxResult getInfo(@PathVariable Long userId)
    {
        return success(sqWxUserService.selectSqWxUserById(userId));
    }

    @Log(title = "企业微信用户", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:wxOrg:add')")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SqWxUser user)
    {
        user.setCreateBy(getUsername());
        return toAjax(sqWxUserService.insertSqWxUser(user));
    }

    @Log(title = "企业微信用户", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:wxOrg:edit')")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SqWxUser user)
    {
        user.setUpdateBy(getUsername());
        return toAjax(sqWxUserService.updateSqWxUser(user));
    }

    @Log(title = "企业微信用户", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:wxOrg:remove')")
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(sqWxUserService.deleteSqWxUserByIds(userIds));
    }
}
