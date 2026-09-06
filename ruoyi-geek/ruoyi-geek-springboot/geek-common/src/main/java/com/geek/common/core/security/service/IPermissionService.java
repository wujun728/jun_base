package com.geek.common.core.security.service;

public interface IPermissionService {

    /**
     * 验证用户是否具备某权限
     * 
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    boolean hasPermi(String permission);

    /**
     * 验证用户是否不具备某权限，与 hasPermi 逻辑相反
     *
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
    default boolean lacksPermi(String permission) {
        return !hasPermi(permission);
    }

    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param permissions 以 PERMISSION_DELIMETER 为分隔符的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    boolean hasAnyPermi(String permissions);

    /**
     * 判断用户是否拥有某个角色
     * 
     * @param role 角色字符串
     * @return 用户是否具备某角色
     */
    boolean hasRole(String role);

    /**
     * 验证用户是否不具备某角色，与 hasRole 逻辑相反。
     *
     * @param role 角色名称
     * @return 用户是否不具备某角色
     */
    default boolean lacksRole(String role) {
        return !hasRole(role);
    }

    /**
     * 验证用户是否具有以下任意一个角色
     *
     * @param roles 以 ROLE_DELIMETER 为分隔符的角色列表
     * @return 用户是否具有以下任意一个角色
     */
    boolean hasAnyRoles(String roles);
}