package com.template.system.controller;

import java.util.List;
import java.util.Set;

import com.template.framework.constant.HttpStatus;
import com.template.system.entity.vo.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.template.framework.constant.SystemConstants;
import com.template.framework.core.domain.Result;
import com.template.system.entity.SysMenu;
import com.template.system.entity.SysUser;
import com.template.system.entity.model.LoginBody;
import com.template.framework.utils.service.SecurityUtils;
import com.template.framework.core.service.SysLoginService;
import com.template.framework.core.service.SysPermissionService;
import com.template.system.service.ISysMenuService;

/**
 * 登录验证
 *
 * @author template
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginBody loginBody)
    {
        Result ajax = Result.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(SystemConstants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public Result getInfo()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        Result ajax = Result.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public Result getRouters(@RequestHeader(value = SystemConstants.HEADER_X_SYSTEM, defaultValue = "0") String sysType)
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId,sysType);
        List<RouterVo> routerVos = menuService.buildMenus(menus);
        if (CollectionUtils.isEmpty(routerVos)) return Result.error(HttpStatus.FORBIDDEN,"没有菜单权限,请联系管理员处理！");
        return Result.success(menuService.buildMenus(menus));
    }
}
