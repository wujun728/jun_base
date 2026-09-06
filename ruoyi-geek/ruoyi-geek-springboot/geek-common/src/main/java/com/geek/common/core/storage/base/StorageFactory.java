package com.geek.common.core.storage.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/** 存储管理器 */
public abstract class StorageFactory<S extends StorageBucket> {
    protected Map<String, S> storageBucketMap = new HashMap<>();
    protected Map<String, Properties> bucketProperties = new HashMap<>();

    protected abstract S createBucket(String name, Properties props);

    protected abstract void validateBucket(S bucket);

    final public S buildBucket(String name, Properties props) {
        S bucket = createBucket(name, props);
        validateBucket(bucket);
        storageBucketMap.put(name, bucket);
        bucketProperties.put(name, props);
        return bucket;
    }
}
