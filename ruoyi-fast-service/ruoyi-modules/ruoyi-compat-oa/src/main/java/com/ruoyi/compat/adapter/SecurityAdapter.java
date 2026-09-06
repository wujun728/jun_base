package com.ruoyi.compat.adapter;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Security适配器
 * 将Spring Security的API调用转换为Sa-Token调用
 *
 * @author ruoyi
 */
public class SecurityAdapter {

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID
     */
    public static Long getUserId() {
        Object loginId = StpUtil.getLoginId();
        if (loginId == null) {
            return null;
        }
        return Long.valueOf(loginId.toString());
    }

    /**
     * 获取当前登录用户名
     *
     * @return 用户名
     */
    public static String getUsername() {
        Object username = StpUtil.getSession().get("username");
        return username != null ? username.toString() : null;
    }

    /**
     * 检查是否已登录
     *
     * @return 是否已登录
     */
    public static boolean isAuthenticated() {
        return StpUtil.isLogin();
    }

    /**
     * 检查是否有指定权限
     *
     * @param permission 权限标识
     * @return 是否有权限
     */
    public static boolean hasPermission(String permission) {
        if (StrUtil.isBlank(permission)) {
            return false;
        }
        return StpUtil.hasPermission(permission);
    }

    /**
     * 检查是否有指定角色
     *
     * @param role 角色标识
     * @return 是否有角色
     */
    public static boolean hasRole(String role) {
        if (StrUtil.isBlank(role)) {
            return false;
        }
        return StpUtil.hasRole(role);
    }

    /**
     * 获取Token
     *
     * @return Token字符串
     */
    public static String getToken() {
        return StpUtil.getTokenValue();
    }

    /**
     * 登出
     */
    public static void logout() {
        StpUtil.logout();
    }
}
