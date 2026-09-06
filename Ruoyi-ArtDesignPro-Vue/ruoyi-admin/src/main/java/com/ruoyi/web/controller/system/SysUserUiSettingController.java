package com.ruoyi.web.controller.system;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ISysUserUiSettingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 当前用户界面偏好
 */
@Tag(name = "用户界面设置")
@RestController
@RequestMapping("/system/user-ui")
public class SysUserUiSettingController extends BaseController
{
    private static final Set<String> ALLOWED_KEYS = Set.of(
        "ui.menu.layout",
        "ui.menu.style",
        "ui.menu.width",
        "ui.menu.dual-show-text",
        "ui.menu.button",
        "ui.fast-enter.enabled",
        "ui.refresh.enabled",
        "ui.theme.mode",
        "ui.theme.color",
        "ui.box.style",
        "ui.container.width",
        "ui.tabs.enabled",
        "ui.tab.style",
        "ui.breadcrumb.enabled",
        "ui.menu.accordion",
        "ui.watermark.enabled",
        "ui.language.enabled",
        "ui.nprogress.enabled",
        "ui.setting-guide.enabled",
        "ui.color-weak.enabled",
        "ui.auto-close.enabled",
        "ui.page-transition",
        "ui.radius"
    );

    @Autowired
    private ISysUserUiSettingService settingService;

    @Operation(summary = "获取用户界面设置")
    @GetMapping
    public AjaxResult get()
    {
        return success(settingService.selectByUserId(getUserId()));
    }

    @PutMapping
    public AjaxResult update(@RequestBody Map<String, String> values)
    {
        Map<String, String> filtered = new LinkedHashMap<>();
        values.forEach((key, value) -> {
            if (ALLOWED_KEYS.contains(key) && value != null)
            {
                filtered.put(key, value);
            }
        });
        settingService.save(getUserId(), getUsername(), filtered);
        return success();
    }
}
