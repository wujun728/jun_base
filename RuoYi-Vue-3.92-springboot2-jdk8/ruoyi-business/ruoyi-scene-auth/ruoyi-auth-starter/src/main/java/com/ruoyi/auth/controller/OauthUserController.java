package com.ruoyi.auth.controller;

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

import com.ruoyi.auth.common.domain.OauthUser;
import com.ruoyi.auth.common.service.IOauthUserService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletResponse;

/**
 * 第三方认证Controller
 * 
 * @author Dftre
 * @date 2024-01-18
 */
@RestController
@RequestMapping("/system/oauth")
@Api(tags = "【第三方认证】管理")
public class OauthUserController extends BaseController {
    @Autowired
    private IOauthUserService oauthUserService;

    /**
     * 查询第三方认证列表
     */
    @ApiOperation("查询第三方认证列表")
    @PreAuthorize("@ss.hasPermi('system:oauth:list')")
    @GetMapping("/list")
    public TableDataInfo list(OauthUser oauthUser) {
        startPage();
        List<OauthUser> list = oauthUserService.selectOauthUserList(oauthUser);
        return getDataTable(list);
    }

    /**
     * 导出第三方认证列表
     */
    @ApiOperation("导出第三方认证列表")
    @PreAuthorize("@ss.hasPermi('system:oauth:export')")
    @Log(title = "第三方认证", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OauthUser oauthUser) {
        List<OauthUser> list = oauthUserService.selectOauthUserList(oauthUser);
        ExcelUtil<OauthUser> util = new ExcelUtil<>(OauthUser.class);
        util.exportExcel(response, list, "第三方认证数据");
    }

    /**
     * 获取第三方认证详细信息
     */
    @ApiOperation("获取第三方认证详细信息")
    @PreAuthorize("@ss.hasPermi('system:oauth:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(oauthUserService.selectOauthUserById(id));
    }

    /**
     * 新增第三方认证
     */
    @ApiOperation("新增第三方认证")
    @PreAuthorize("@ss.hasPermi('system:oauth:add')")
    @Log(title = "第三方认证", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OauthUser oauthUser) {
        return toAjax(oauthUserService.insertOauthUser(oauthUser));
    }

    /**
     * 修改第三方认证
     */
    @ApiOperation("修改第三方认证")
    @PreAuthorize("@ss.hasPermi('system:oauth:edit')")
    @Log(title = "第三方认证", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OauthUser oauthUser) {
        return toAjax(oauthUserService.updateOauthUser(oauthUser));
    }

    /**
     * 删除第三方认证
     */
    @ApiOperation("删除第三方认证")
    @PreAuthorize("@ss.hasPermi('system:oauth:remove')")
    @Log(title = "第三方认证", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable(name = "ids") Long[] ids) {
        return toAjax(oauthUserService.deleteOauthUserByIds(ids));
    }
}
