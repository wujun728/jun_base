package com.template.framework.config.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author template
 */
@Component
public class SystemConfig {
    /**
     * 项目名称
     */
    private String name;

    /**
     * 上传路径
     */
    private static String profile;

    /**
     * 获取地址开关
     */
    private static boolean addressEnabled;

    /**
     * 验证码开关
     */
    private static boolean captchaEnabled;

    /**
     * 是否开启用户注册功能
     */
    private static boolean registerUser;

    /**
     * 账号初始密码
     */
    private static String initPassword;

    /**
     * 验证码类型
     */
    private static String captchaType;


    @Value("${system.name}")
    public void setName(String name) {
        this.name = name;
    }

    @Value("${system.profile}")
    public void setProfile(String profile) {
        SystemConfig.profile = profile;
    }

    @Value("${system.initPassword}")
    public void setInitPassword(String initPassword) {
        SystemConfig.initPassword = initPassword;
    }

    @Value("${system.registerUser}")
    public void setRegisterUser(boolean registerUser) {
        SystemConfig.registerUser = registerUser;
    }

    @Value("${system.captchaEnabled}")
    public void setCaptchaEnabled(boolean captchaEnabled) {
        SystemConfig.captchaEnabled = captchaEnabled;
    }

    @Value("${system.captchaType}")
    public void setCaptchaType(String captchaType) {
        SystemConfig.captchaType = captchaType;
    }

    @Value("${system.addressEnabled}")
    public void setAddressEnabled(boolean addressEnabled) {
        SystemConfig.addressEnabled = addressEnabled;
    }

    public String getName() {
        return name;
    }

    public static String getProfile() {
        return profile;
    }

    public static String getCaptchaType() {
        return captchaType;
    }

    public static boolean getAddressEnabled() {
        return addressEnabled;
    }

    public static boolean getCaptchaEnabled() {
        return captchaEnabled;
    }

    public static boolean getRegisterUser() {
        return registerUser;
    }

    public static String getInitPassword() {
        return initPassword;
    }

    /**
     * 获取导入上传路径
     */
    public static String getImportPath() {
        return getProfile() + "/import";
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return getProfile() + "/upload";
    }
}
