package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
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
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 参数配置 信息操作处理
 * 
 * @author ruoyi
 */
@Tag(name = "参数配置")
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController
{
    private static final Map<String, String> SITE_CONFIGS = new LinkedHashMap<>();

    static
    {
        SITE_CONFIGS.put("site.name", "系统名称");
        SITE_CONFIGS.put("site.description", "站点简介");
        SITE_CONFIGS.put("site.login.title", "登录欢迎标题");
        SITE_CONFIGS.put("site.login.description", "登录欢迎描述");
        SITE_CONFIGS.put("site.login-left-title", "登录页左侧标题");
        SITE_CONFIGS.put("site.login-left-sub-title", "登录页左侧描述");
        SITE_CONFIGS.put("sys.index.skinName", "默认皮肤样式");
        SITE_CONFIGS.put("sys.index.sideTheme", "侧边栏主题");
        SITE_CONFIGS.put("site.watermark.content", "公共水印内容");
        SITE_CONFIGS.put("site.watermark.mode", "水印内容模式");
        SITE_CONFIGS.put("site.watermark.show-time", "水印叠加时间");
        SITE_CONFIGS.put("sys.account.captchaEnabled", "验证码开关");
        SITE_CONFIGS.put("sys.account.captchaType", "验证码类型");
        SITE_CONFIGS.put("sys.account.behaviorCaptchaType", "行为验证码类型");
        SITE_CONFIGS.put("sys.account.registerUser", "开放用户注册");
        SITE_CONFIGS.put("sys.login.blackIPList", "登录黑名单");
        SITE_CONFIGS.put("security.access-token-hours", "访问令牌有效时长");
        SITE_CONFIGS.put("security.max-failed-login-count", "登录失败锁定阈值");
        SITE_CONFIGS.put("security.account-lock-minutes", "账号锁定时长");
        SITE_CONFIGS.put("security.password-min-length", "密码最小长度");
        SITE_CONFIGS.put("security.password-max-length", "密码最大长度");
        SITE_CONFIGS.put("security.password-require-uppercase", "密码要求大写字母");
        SITE_CONFIGS.put("security.password-require-lowercase", "密码要求小写字母");
        SITE_CONFIGS.put("security.password-require-number", "密码要求数字");
        SITE_CONFIGS.put("security.password-require-special", "密码要求特殊字符");
        SITE_CONFIGS.put("sys.user.initPassword", "用户初始密码");
        SITE_CONFIGS.put("sys.account.initPasswordModify", "初始密码修改策略");
        SITE_CONFIGS.put("sys.account.passwordValidateDays", "账号密码更新周期");
        SITE_CONFIGS.put("sys.account.chrtype", "密码字符范围");
    }

    @Autowired
    private ISysConfigService configService;

    /**
     * 获取前端站点配置。登录页需要匿名读取。
     */
    @Anonymous
    @Operation(summary = "获取站点配置")
    @GetMapping("/site")
    public AjaxResult site()
    {
        Map<String, String> configs = new LinkedHashMap<>();
        SITE_CONFIGS.keySet().forEach(key -> configs.put(key, configService.selectConfigByKey(key)));
        return success(configs);
    }

    /**
     * 保存前端站点配置。
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "站点配置", businessType = BusinessType.UPDATE)
    @Operation(summary = "保存站点配置")
    @PutMapping("/site")
    public AjaxResult updateSite(@RequestBody Map<String, String> values)
    {
        SITE_CONFIGS.forEach((key, name) -> {
            if (!values.containsKey(key))
            {
                return;
            }
            SysConfig config = new SysConfig();
            config.setConfigKey(key);
            config.setConfigName(name);
            config.setConfigValue(values.get(key));
            config.setConfigType("Y");
            config.setRemark("Art 前端站点配置");
            SysConfig current = configService.selectConfigByKeyInfo(key);
            if (current == null)
            {
                config.setCreateBy(getUsername());
                configService.insertConfig(config);
            }
            else
            {
                config.setConfigId(current.getConfigId());
                config.setUpdateBy(getUsername());
                configService.updateConfig(config);
            }
        });
        return success();
    }

    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @Operation(summary = "查询参数配置列表")
    @GetMapping("/list")
    public TableDataInfo list(SysConfig config)
    {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @Operation(summary = "导出参数配置")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysConfig config)
    {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        util.exportExcel(response, list, "参数数据");
    }

    /**
     * 根据参数编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @Operation(summary = "获取参数配置详情")
    @GetMapping(value = "/{configId}")
    @io.swagger.v3.oas.annotations.Parameter(description = "参数ID")
    public AjaxResult getInfo(@PathVariable Long configId)
    {
        return success(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @Operation(summary = "根据参数键名查询参数值")
    @GetMapping(value = "/configKey/{configKey}")
    @io.swagger.v3.oas.annotations.Parameter(description = "参数键名")
    public AjaxResult getConfigKey(@PathVariable String configKey)
    {
        return success(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @Operation(summary = "新增参数配置")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysConfig config)
    {
        if (!configService.checkConfigKeyUnique(config))
        {
            return error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(getUsername());
        return toAjax(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @Operation(summary = "修改参数配置")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysConfig config)
    {
        if (!configService.checkConfigKeyUnique(config))
        {
            return error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(getUsername());
        return toAjax(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @Operation(summary = "删除参数配置")
    @DeleteMapping("/{configIds}")
    @io.swagger.v3.oas.annotations.Parameter(description = "参数ID数组")
    public AjaxResult remove(@PathVariable Long[] configIds)
    {
        configService.deleteConfigByIds(configIds);
        return success();
    }

    /**
     * 刷新参数缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @Operation(summary = "刷新缓存")
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache()
    {
        configService.resetConfigCache();
        return success();
    }
}
