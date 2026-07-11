package com.ruoyi.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.file.*;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil extends ObjectSwitchHelper {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Properties properties;
    private String[] resourcesPaths;
    private String properiesName = "";

    public PropertiesUtil() {}

    public static PropertiesUtil create(String... resourcesPaths) {
        return new PropertiesUtil(resourcesPaths);
    }

    public PropertiesUtil(String... resourcesPaths) {
        load(resourcesPaths);
    }

    public void load(String... resourcesPaths) {
        this.resourcesPaths = resourcesPaths;
        properties = new Properties();
        for (String location : resourcesPaths) {
            logger.debug("Loading properties file from:" + location);
            try (InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream(location)) {
                if (is != null) properties.load(is);
            } catch (IOException ex) {
                logger.info("Could not load properties from path:" + location + ", " + ex.getMessage());
            }
        }
    }

    public void setOptProperiesName(String properiesName) {
        this.properiesName = properiesName;
    }

    @Override
    public Object get(String key) {
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        return "";
    }

    public String getAbsolutePath(String filename) {
        if (!isAbsolutePath(filename)) {
            java.net.URL url = PropertiesUtil.class.getClassLoader().getResource(filename);
            if (url != null) filename = url.getPath();
        }
        return filename;
    }

    private boolean isAbsolutePath(String path) {
        return path.startsWith("/") || path.indexOf(':') > 0;
    }

    private void createFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    @Override
    public void set(String key, Object value) {
        if (StringUtils.isEmpty(this.properiesName)) {
            this.properiesName = this.resourcesPaths[0];
        }
        try {
            createFile(getAbsolutePath(this.properiesName));
            Properties p = new Properties();
            try (InputStream is = new FileInputStream(getAbsolutePath(this.properiesName))) {
                p.load(is);
            }
            try (OutputStream os = new FileOutputStream(getAbsolutePath(this.properiesName))) {
                p.setProperty(key, value + "");
                p.store(os, "====update====");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.properiesName = "";
    }

    public void set(Map<String, Object> dataMap) {
        if (StringUtils.isEmpty(this.properiesName)) {
            this.properiesName = this.resourcesPaths[0];
        }
        try {
            createFile(getAbsolutePath(this.properiesName));
            Properties p = new Properties();
            try (InputStream is = new FileInputStream(getAbsolutePath(this.properiesName))) {
                p.load(is);
            }
            try (OutputStream os = new FileOutputStream(getAbsolutePath(this.properiesName))) {
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                    p.setProperty(entry.getKey(), entry.getValue() + "");
                }
                p.store(os, "====update====");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.properiesName = "";
    }

    @Override
    public boolean remove(String key) {
        if (StringUtils.isEmpty(this.properiesName)) {
            this.properiesName = this.resourcesPaths[0];
        }
        try {
            createFile(getAbsolutePath(this.properiesName));
            Properties p = new Properties();
            try (InputStream is = new FileInputStream(getAbsolutePath(this.properiesName))) {
                p.load(is);
            }
            try (OutputStream os = new FileOutputStream(getAbsolutePath(this.properiesName))) {
                p.remove(key);
                p.store(os, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.properiesName = "";
        return false;
    }
}
