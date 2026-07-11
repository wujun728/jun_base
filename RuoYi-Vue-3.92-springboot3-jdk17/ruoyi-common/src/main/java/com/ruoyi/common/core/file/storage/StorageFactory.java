package com.ruoyi.common.core.file.storage;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;

public abstract class StorageFactory<P, B extends StorageBucket> {
    public abstract B createBucket(String name, P props);
    
    public String getRelativePath(String path) {
        if (StringUtils.isEmpty(path)) return "";
        String dir = System.getProperty("user.dir");
        if (path.startsWith(dir)) return path.substring(dir.length() + 1);
        return path;
    }
}
