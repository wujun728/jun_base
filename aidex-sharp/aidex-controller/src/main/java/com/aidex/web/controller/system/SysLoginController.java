package com.aidex.web.controller.system;

import com.aidex.common.constant.Constants;
import com.aidex.common.core.domain.AjaxResult;
import com.aidex.common.core.domain.entity.SysMenu;
import com.aidex.common.core.domain.entity.SysUser;
import com.aidex.common.core.domain.model.LoginBody;
import com.aidex.common.core.domain.model.LoginUser;
import com.aidex.common.core.redis.RedisCache;
import com.aidex.common.utils.SecurityUtils;
import com.aidex.common.utils.ServletUtils;
import com.aidex.framework.web.service.SysLoginService;
import com.aidex.framework.web.service.SysPermissionService;
import com.aidex.framework.web.service.TokenService;
import com.aidex.system.domain.SysNotice;
import com.aidex.system.domain.SysPortalConfig;
import com.aidex.system.service.SysMenuService;
import com.aidex.system.service.SysNoticeService;
import com.aidex.system.service.SysPortalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
public class SysLoginController {

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private SysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private SysNoticeService sysNoticeService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysPortalConfigService sysPortalConfigService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        //获取用户自定义首页
        Map<String, Object> resultMap = sysPortalConfigService.findUserConfigList(user);
        if (resultMap != null) {
            List<SysPortalConfig> userPortalConfig = (List<SysPortalConfig>) resultMap.get("portalList");
            SysPortalConfig defaultSysPortalConfig = (SysPortalConfig) resultMap.get("default");
            ajax.put("userPortalConfig", userPortalConfig);
            ajax.put("defaultPortalConfig", defaultSysPortalConfig);
        }
        //获取用户待读通知公告
        List<SysNotice> sysNoticeList = sysNoticeService.getNoticeListByUserId(user.getId());
        if (CollectionUtils.isEmpty(sysNoticeList)) {
            sysNoticeList = new ArrayList<SysNotice>();
        }
        String lincenseInfo = redisCache.getStringValue("aidex.license.info");
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        ajax.put("lincenseInfo", lincenseInfo);
        ajax.put("sysNoticeList", sysNoticeList);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        // 用户信息
        SysUser user = SecurityUtils.getLoginUser().getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
