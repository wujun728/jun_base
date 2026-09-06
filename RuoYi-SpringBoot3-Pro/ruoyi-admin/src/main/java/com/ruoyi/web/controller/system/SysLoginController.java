package com.ruoyi.web.controller.system;

import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sign.RsaUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.system.service.ISysMenuService;

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
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysUserService userService;

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
        String token = null;
        String username = loginBody.getUsername();

        String msg = ifLockUser(username);
        if (null != msg)
            throw new RuntimeException(msg);

        try {
            token = loginService.login(username, RsaUtils.decryptByPrivateKey(loginBody.getPassword()),
                    loginBody.getCode(),
                    loginBody.getUuid(), loginBody.getSmsCode());
            userService.resetTryCount(username, 0);
        } catch (Exception e) {
            SysUser user = userService.selectUserByUserName(username);
            if (null != user && StringUtils.equals("0", user.getDelFlag())) {
                Integer tryCount = null == user.getTryCount() ? 1 : (user.getTryCount() + 1);
                msg = ifTryGtCount(tryCount);
                if (null != msg) {
                    redisCache.setCacheObject(username + "-tryGtCount", DateUtil.now());
                } else {
                    msg = e.getMessage();
                    userService.resetTryCount(username, tryCount);
                }
            } else {
                msg = e.getMessage();
            }

            throw new RuntimeException(msg);
        }
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
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        if (!loginUser.getPermissions().equals(permissions)) {
            loginUser.setPermissions(permissions);
            tokenService.refreshToken(loginUser);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        ajax.put("isDefaultModifyPwd", initPasswordIsModify(user.getPwdUpdateDate()));
        ajax.put("isPasswordExpired", passwordIsExpiration(user.getPwdUpdateDate()));
        return ajax;
    }

    public void unLockUser(String username) {
        userService.resetTryCount(username, 0);
        redisCache.deleteObject(username + "-tryGtCount");
    }

    public String ifLockUser(String username) {
        Integer[] configs = getTryLoginCountConfig();
        if (configs == null || configs[1] == null || configs[1] == 0) {
            return null;
        }

        String tryGtCountUsername = username + "-tryGtCount";
        if (redisCache.hasKey(tryGtCountUsername)) {
            String datetime = redisCache.getCacheObject(tryGtCountUsername);
            long betweenMinute = DateUtil.between(DateUtil.parse(datetime), new Date(), DateUnit.MINUTE);
            if (betweenMinute > configs[1]) {
                unLockUser(username);
                return null;
            } else {
                return "连续" + configs[0] + "次登录失败，请" + configs[1] + "分钟后再试";
            }
        }

        return null;
    }

    public Integer[] getTryLoginCountConfig() {
        String tryLoginCount = configService.selectConfigByKey("sys.account.tryLoginCount");
        if (StringUtils.equals("0", tryLoginCount)) {
            return null;
        }
        String[] arr = StringUtils.split(tryLoginCount, "-");
        if (arr.length == 2) {
            if (NumberUtil.isInteger(arr[0]) && NumberUtil.isInteger(arr[1])) {
                Integer[] configs = new Integer[2];
                configs[0] = Convert.toInt(arr[0]);
                configs[1] = Convert.toInt(arr[1]);
                return configs;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String ifTryGtCount(Integer tryCount) {
        Integer[] configs = getTryLoginCountConfig();
        if (configs == null) {
            return null;
        }
        if (configs[0] != null && configs[0] > 0) {
            if (tryCount != null && tryCount >= configs[0]) {
                return "连续" + configs[0] + "次登录失败，请" + configs[1] + "分钟后再试";
            }
        }

        return null;
    }

    // 检查初始密码是否提醒修改
    public boolean initPasswordIsModify(Date pwdUpdateDate) {
        Integer initPasswordModify = Convert.toInt(configService.selectConfigByKey("sys.account.initPasswordModify"));
        return initPasswordModify != null && initPasswordModify == 1 && pwdUpdateDate == null;
    }

    // 检查密码是否过期
    public boolean passwordIsExpiration(Date pwdUpdateDate) {
        Integer passwordValidateDays = Convert
                .toInt(configService.selectConfigByKey("sys.account.passwordValidateDays"));
        if (passwordValidateDays != null && passwordValidateDays > 0) {
            if (StringUtils.isNull(pwdUpdateDate)) {
                // 如果从未修改过初始密码，直接提醒过期
                return true;
            }
            Date nowDate = DateUtils.getNowDate();
            return DateUtils.differentDaysByMillisecond(nowDate, pwdUpdateDate) > passwordValidateDays;
        }
        return false;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
