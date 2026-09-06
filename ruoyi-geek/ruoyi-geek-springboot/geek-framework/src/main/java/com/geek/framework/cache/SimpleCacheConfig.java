package com.geek.framework.cache;

import java.time.Duration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ConditionalOnProperty(prefix = "spring.cache", name = { "type" }, havingValue = "simple", matchIfMissing = false)
public class SimpleCacheConfig {

    @Bean
    SimpleCacheManager cacheManager() {
        return new SimpleCacheManager();
    }

    @Bean(destroyMethod = "stop")
    SimpleCacheExpiredEntryCleaner simpleCacheExpiredEntryCleaner(SimpleCacheManager cacheManager, Environment env) {
        Duration interval = Duration.ofSeconds(30);
        int maxScanPerCache = 5000;
        return new SimpleCacheExpiredEntryCleaner(cacheManager, interval, maxScanPerCache);
    }
}
