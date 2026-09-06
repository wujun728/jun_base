package com.geek.system.domain;

import com.geek.common.utils.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 缓存信息
 * 
 * @author geek
 */
@Schema(title = "缓存信息")
@Data
public class SysCache {
    /** 缓存名称 */
    @Schema(title = "缓存名称")
    private String cacheName = "";

    /** 缓存键名 */
    @Schema(title = "缓存键名")
    private String cacheKey = "";

    /** 缓存内容 */
    @Schema(title = "缓存内容")
    private String cacheValue = "";

    /** 备注 */
    @Schema(title = "备注")
    private String remark = "";

    public SysCache() {

    }

    public SysCache(String cacheName, String remark) {
        this.cacheName = cacheName;
        this.remark = remark;
    }

    public SysCache(String cacheName, String cacheKey, String cacheValue) {
        this.cacheName = StringUtils.replace(cacheName, ":", "");
        this.cacheKey = StringUtils.replace(cacheKey, cacheName, "");
        this.cacheValue = cacheValue;
    }
}
