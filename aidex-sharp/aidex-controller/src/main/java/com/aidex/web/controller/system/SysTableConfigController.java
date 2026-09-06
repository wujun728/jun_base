package com.aidex.web.controller.system;

import com.aidex.common.annotation.Log;
import com.aidex.common.core.controller.BaseController;
import com.aidex.common.core.domain.R;
import com.aidex.common.core.domain.model.LoginUser;
import com.aidex.common.core.page.PageDomain;
import com.aidex.common.enums.BusinessType;
import com.aidex.common.utils.SecurityUtils;
import com.aidex.common.utils.poi.ExcelUtil;
import com.aidex.system.domain.SysTableConfig;
import com.aidex.system.service.SysTableConfigService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 个性化配置Controller
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-03-31
 */
@RestController
@RequestMapping("/system/sysTableConfig")
public class SysTableConfigController extends BaseController
{
    @Autowired
    private SysTableConfigService sysTableConfigService;

    /**
     * 查询个性化配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:sysTableConfig:list')")
    @GetMapping("/list")
    public R<PageInfo> list(SysTableConfig sysTableConfig, HttpServletRequest request, HttpServletResponse response)
    {
        sysTableConfig.setPage(new PageDomain(request, response));
        return R.data(sysTableConfigService.findPage(sysTableConfig));
    }

    /**
     * 获取个性化配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:sysTableConfig:query')")
    @GetMapping(value = "/{id}")
    public R<SysTableConfig> detail(@PathVariable("id") String id)
    {
        return R.data(sysTableConfigService.get(id));
    }
    @GetMapping(value = "/getInfoByTableKey/{tableKey}")
    public R<SysTableConfig> getInfoByTableKey(@PathVariable("tableKey") String tableKey)
    {
        return R.data(sysTableConfigService.getInfoByTableKey(tableKey));
    }
    /**
     * 新增个性化配置
     */
    @Log(title = "个性化配置", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody @Validated  SysTableConfig sysTableConfig)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        sysTableConfig.setUserId(loginUser.getUser().getId());
        sysTableConfigService.save(sysTableConfig);
        return R.data(sysTableConfig);
    }

    /**
     * 修改个性化配置
     */
    @Log(title = "个性化配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody @Validated SysTableConfig sysTableConfig)
    {
        sysTableConfigService.save(sysTableConfig);
        return R.data(sysTableConfig);
    }

    /**
     * 删除个性化配置
     */
    @PreAuthorize("@ss.hasPermi('system:sysTableConfig:remove')")
    @Log(title = "个性化配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tableKeys}")
    public R remove(@PathVariable String[] ids)
    {
        return R.status(sysTableConfigService.deleteSysTableConfigByIds(ids));
    }



    /**
     * 导出个性化配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:sysTableConfig:export')")
    @Log(title = "个性化配置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public R export(SysTableConfig sysTableConfig)
    {
        List<SysTableConfig> list = sysTableConfigService.findList(sysTableConfig);
        ExcelUtil<SysTableConfig> util = new ExcelUtil<SysTableConfig>(SysTableConfig.class);
        return util.exportExcel(list, "sysTableConfig");
    }

}