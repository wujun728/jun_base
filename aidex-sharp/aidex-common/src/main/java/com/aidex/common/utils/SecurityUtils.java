package com.aidex.common.utils;

import com.aidex.common.constant.HttpStatus;
import com.aidex.common.core.domain.model.LoginUser;
import com.aidex.common.exception.SysException;
import com.aidex.common.utils.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 安全服务工具类
 *
 * @author ruoyi
 */
public class SecurityUtils {

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            if (null != getAuthentication()) {
                Object principal = getAuthentication().getPrincipal();
                if (principal instanceof String && principal.toString().equals("anonymousUser")) {
                    //匿名用户
                    return new LoginUser();
                }
                if (principal != null) {
                    LoginUser user = (LoginUser) principal;
                    if (user != null) {
                        return user;
                    }
                    return new LoginUser();
                }
            }
            return new LoginUser();
        } catch (Exception e) {
            throw new SysException(HttpStatus.UNAUTHORIZED, "获取用户信息异常");
        }
    }

    /**
     * 用户ID
     **/
    public static String getUserId() {
        try {
            return getLoginUser().getUser().getId();
        } catch (Exception e) {
            throw new SysException(HttpStatus.UNAUTHORIZED, "获取用户ID异常");
        }
    }

    /**
     * 获取部门ID
     **/
    public static String getDeptId() {
        try {
            return getLoginUser().getUser().getDeptId();
        } catch (Exception e) {
            throw new SysException(HttpStatus.UNAUTHORIZED, "获取部门ID异常");
        }
    }

    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            throw new SysException(HttpStatus.UNAUTHORIZED, "获取用户账户异常");
        }
    }


    /**
     * 获取登录IP地址
     **/
    public static String getIpaddr() {
        return getLoginUser().getIpaddr();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(String userId) {
        return StringUtils.isNotBlank(userId) && "1".equals(userId);
    }
}
