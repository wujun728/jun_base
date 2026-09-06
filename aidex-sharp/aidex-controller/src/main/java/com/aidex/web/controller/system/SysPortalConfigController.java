package com.aidex.web.controller.system;

import com.aidex.common.annotation.Log;
import com.aidex.common.core.controller.BaseController;
import com.aidex.common.core.domain.R;
import com.aidex.common.core.domain.entity.SysRole;
import com.aidex.common.core.domain.entity.SysUser;
import com.aidex.common.core.domain.model.LoginUser;
import com.aidex.common.core.page.PageDomain;
import com.aidex.common.enums.BusinessType;
import com.aidex.common.utils.SecurityUtils;
import com.aidex.common.utils.StringUtils;
import com.aidex.common.utils.poi.ExcelUtil;
import com.aidex.system.domain.SysPortalConfig;
import com.aidex.system.domain.SysPortlet;
import com.aidex.system.service.ISysRoleService;
import com.aidex.system.service.ISysUserService;
import com.aidex.system.service.SysPortalConfigService;
import com.aidex.system.service.SysPortletService;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多栏目门户配置Controller
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-05-11
 */
@RestController
@RequestMapping("/system/sysPortalConfig")
public class SysPortalConfigController extends BaseController {
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private SysPortalConfigService sysPortalConfigService;

    @Autowired
    private SysPortletService sysPortletService;

    @Autowired
    private ISysUserService userService;

    //目前写死作为系统标识
    private static String systemDefinedId = "app1";

    /**
     * 查询多栏目门户配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:sysPortalConfig:list')")
    @GetMapping("/list")
    public R<PageInfo> list(SysPortalConfig sysPortalConfig, HttpServletRequest request, HttpServletResponse response) {
        sysPortalConfig.setPage(new PageDomain(request, response));
        return R.data(sysPortalConfigService.findPage(sysPortalConfig));
    }

    /**
     * 获取多栏目门户配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:sysPortalConfig:query')")
    @GetMapping(value = "/{id}")
    public R detail(@PathVariable("id") String id) {
        Map<String, Object> resultMap = new HashMap<String, Object>(2);
        List<SysRole> roles = roleService.selectRoleAll();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        resultMap.put("roles", SysUser.isAdmin(loginUser.getUser().getId()) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        if (StringUtils.isNotBlank(id) && !"undefined".equals(id)) {
            SysPortalConfig sysPortalConfig = sysPortalConfigService.get(id);
            resultMap.put("data", sysPortalConfig);
            if ("U".equals(sysPortalConfig.getApplicationRange())) {
                JSONObject userIdsObj = new JSONObject();
                userIdsObj.put("userIds", sysPortalConfig.getResourceId());
                List<Map<String, Object>> listMap = userService.getUserInfoByIds(userIdsObj);
                resultMap.put("listMap", listMap);
            }
        }
        return R.data(resultMap);
    }

    /**
     * 新增多栏目门户配置
     */
    @Log(title = "多栏目门户配置", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody @Validated SysPortalConfig sysPortalConfig) {
        sysPortalConfig.setSystemDefinedId(systemDefinedId);
        sysPortalConfig.setStatus("0");
        sysPortalConfigService.insertOrUpdateSysPortalConfig(sysPortalConfig);
        return R.data(sysPortalConfig);
    }

    /**
     * 修改多栏目门户配置
     */
    @Log(title = "多栏目门户配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody @Validated SysPortalConfig sysPortalConfig) {
        sysPortalConfigService.insertOrUpdateSysPortalConfig(sysPortalConfig);
        return R.data(sysPortalConfig);
    }

    /**
     * 删除多栏目门户配置
     */
    @Log(title = "多栏目门户配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        return R.status(sysPortalConfigService.deleteSysPortalConfigByIds(ids));
    }

    /**
     * 获取最大编号
     */
    @PreAuthorize("@ss.hasPermi('system:sysPortalConfig:query')")
    @GetMapping("/findMaxSort")
    public R findMaxSort() {
        return R.data(sysPortalConfigService.findMaxSort(new SysPortalConfig()));
    }

    /**
     * 校验小页编码的唯一性
     */
    @PreAuthorize("@ss.hasPermi('system:sysPortalConfig:query')")
    @Log(title = "多栏目门户配置", businessType = BusinessType.CHECK)
    @GetMapping(value = {"/checkCodeUnique/{code}/{id}", "/checkCodeUnique/{code}"})
    public R checkCodeUnique(@NotBlank(message = "小页编码不允许为空") @PathVariable("code") String code, @PathVariable(value = "id", required = false) String id) {
        SysPortalConfig sysPortalConfig = new SysPortalConfig();
        sysPortalConfig.setCode(code);
        sysPortalConfig.setId(id);
        sysPortalConfigService.checkCodeUnique(sysPortalConfig);
        return R.status(Boolean.TRUE);
    }

    /**
     * 导出多栏目门户配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:sysPortalConfig:export')")
    @Log(title = "多栏目门户配置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public R export(SysPortalConfig sysPortalConfig) {
        List<SysPortalConfig> list = sysPortalConfigService.findList(sysPortalConfig);
        ExcelUtil<SysPortalConfig> util = new ExcelUtil<SysPortalConfig>(SysPortalConfig.class);
        return util.exportExcel(list, "sysPortalConfig");
    }

    /**
     * 获取多栏目门户配置详细信息
     */
    @GetMapping("/getConfigAndPortalList/{id}")
    public R getConfigAndPortalList(@PathVariable(value = "id", required = false) String id) {
        Map<String, Object> resultMap = new HashMap<String, Object>(2);
        List<SysPortlet> sysPortletList = sysPortletService.getPortletListByAuth(id);
        resultMap.put("data", sysPortalConfigService.get(id));
        resultMap.put("sysPortletList", sysPortletList);
        return R.data(resultMap);
    }

    @GetMapping("/getConfiglListByLoginUser")
    public R<List<SysPortalConfig>> getConfiglListByLoginUser(HttpServletRequest request, HttpServletResponse response) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Map<String, Object> resultMap = sysPortalConfigService.findUserConfigList(loginUser.getUser());
        List<SysPortalConfig> userPortalConfig = null;
        if (resultMap != null) {
            userPortalConfig = (List<SysPortalConfig>) resultMap.get("portalList");
        }
        return R.data(userPortalConfig);
    }

    @Log(title = "多栏目门户配置", businessType = BusinessType.UPDATE)
    @PutMapping("/updateDefaultPortalConfig")
    public R updateDefaultPortalConfig(@RequestBody @Validated SysPortalConfig sysPortalConfig) {
        sysPortalConfigService.updateDefaultPortalConfig(sysPortalConfig);
        return R.data(sysPortalConfig);
    }

    /**
     * 获取模板列表
     */
    @GetMapping("/getPortalTemplateList")
    public R<List<SysPortalConfig>> getPortalTemplateList(HttpServletRequest request, HttpServletResponse response) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<SysPortalConfig> sysPortalConfigList = sysPortalConfigService.getPortalTemplateList(loginUser.getUser());
        return R.data(sysPortalConfigList);
    }


}
