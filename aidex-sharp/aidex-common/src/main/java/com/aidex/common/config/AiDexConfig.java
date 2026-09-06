package com.aidex.common.config;

import com.aidex.common.utils.DateUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 读取项目相关配置
 * 
 * @author aidex
 */
@Component
@ConfigurationProperties(prefix = "aidex")
public class AiDexConfig
{

    /**
     * 文件存储后缀
     */
    private static String filePathSubfix = File.separator + DateUtils.getDate("yyyy") + File.separator + DateUtils.getDate("MM") + File.separator + DateUtils.getDate("dd");

    /** 项目名称 */
    private String name;

    /** 版本 */
    private String version;

    /** 版权年份 */
    private String copyrightYear;

    /** 实例演示开关 */
    private boolean demoEnabled;

    /** 上传路径 */
    private static String profile;

    /** 上传路径 */
    private static String dbName;

    /** 获取地址开关 */
    private static boolean addressEnabled;

    /**
     * 验证码类型
     */
    private String captchaType;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getCopyrightYear()
    {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear)
    {
        this.copyrightYear = copyrightYear;
    }

    public boolean isDemoEnabled()
    {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled)
    {
        this.demoEnabled = demoEnabled;
    }

    public static String getProfile()
    {
        return profile;
    }

    public void setProfile(String profile)
    {
        AiDexConfig.profile = profile;
    }

    public static boolean isAddressEnabled()
    {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled)
    {
        AiDexConfig.addressEnabled = addressEnabled;
    }

    /**
     /**
     * 获取导入上传路径
     */
    public static String getImportPath() {
        return getProfile() + "/import" + filePathSubfix;
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getProfile() + File.separator + "avatar" + filePathSubfix;
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getProfile() + File.separator + "download" + filePathSubfix;
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        //根据年月日分组
        return getProfile() + File.separator + "upload" + filePathSubfix;
    }

    public static String getDbName() {
        return dbName;
    }

    public static void setDbName(String dbName) {
        AiDexConfig.dbName = dbName;
    }

    public String getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(String captchaType) {
        this.captchaType = captchaType;
    }
}
