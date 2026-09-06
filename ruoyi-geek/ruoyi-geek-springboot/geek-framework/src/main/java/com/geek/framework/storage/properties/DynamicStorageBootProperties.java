package com.geek.framework.storage.properties;

import java.util.Properties;

import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "storage")
public class DynamicStorageBootProperties {
    private LinkedMap<String, Properties> buckets = new LinkedMap<>();
    private String primary;
}
