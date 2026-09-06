package com.geek.common.core.cache;

import java.io.Serializable;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 包装带有单次写入 TTL 的值。TTL 毫秒为相对时长，由 ExpiryPolicy 在创建/更新时读取。
 */
@Data
public final class TimedValue<T> implements Serializable {
    private final T data;
    private final long ttlMillis;
    private final Date createTime;

    public TimedValue(T data, long ttlMillis) {
        this.data = data;
        this.ttlMillis = ttlMillis;
        this.createTime = new Date(System.currentTimeMillis());
    }

    @JsonCreator
    public TimedValue(
            @JsonProperty("data") T data,
            @JsonProperty("ttlMillis") long ttlMillis,
            @JsonProperty("createTime") Date createTime) {
        this.data = data;
        this.ttlMillis = ttlMillis;
        this.createTime = createTime; // 反序列化时用缓存的createTime，不重新生成
    }

    public boolean isExpired() {
        if (ttlMillis <= 0) {
            return false;
        }
        long now = System.currentTimeMillis();
        return now - createTime.getTime() >= ttlMillis;
    }
}
