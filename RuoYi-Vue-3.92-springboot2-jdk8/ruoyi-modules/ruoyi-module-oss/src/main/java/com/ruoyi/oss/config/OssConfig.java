package com.ruoyi.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.ruoyi.oss.config
 * @title:
 * @description: OSS配置 * @date: 2018/4/26 9:43
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
@ConfigurationProperties(prefix = "oss")
public class OssConfig {

    //使用的上传存储空间,local本地,aliyun:阿里云
    String clientType = "";
    //默认上传目录
    String baseDir = "";
    //允许的文件扩展名
    String allowedExtension = "";
    //最大文件大小 50M
    long maxSize = 52428800;
    //是否需要按照日期存放文件
    boolean needDatePath = false;
    ;

    private AliyunConfig aliyun;

    private LocalConfig local;

    public AliyunConfig getAliyun() {
        return aliyun;
    }

    public void setAliyun(AliyunConfig aliyun) {
        this.aliyun = aliyun;
    }

    public LocalConfig getLocal() {
        return local;
    }

    public void setLocal(LocalConfig local) {
        this.local = local;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getAllowedExtension() {
        return allowedExtension;
    }

    public void setAllowedExtension(String allowedExtension) {
        this.allowedExtension = allowedExtension;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public boolean isNeedDatePath() {
        return needDatePath;
    }

    public void setNeedDatePath(boolean needDatePath) {
        this.needDatePath = needDatePath;
    }
}
