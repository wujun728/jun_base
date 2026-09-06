package com.ruoyi.generator.util;

import java.util.Properties;

import org.apache.velocity.app.Velocity;
import com.ruoyi.common.constant.Constants;

/**
 * VelocityEngine工厂
 *
 * @author ruoyi
 */
public class VelocityInitializer {
    /**
     * 初始化vm方法
     */
    public static void initVelocity() {
//        Properties p = new Properties();
//        try {
//            // 加载classpath目录下的vm文件
//            p.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//            // 定义字符集
//            p.setProperty(Velocity.INPUT_ENCODING, Constants.UTF8);
//            // 初始化Velocity引擎，指定配置Properties
//            Velocity.init(p);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        Properties p = new Properties();
        try {
            // 加载classpath目录下的vm文件
            p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            p.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
            // 设置输入编码
            p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
            // 设置资源加载路径
            p.setProperty("file.resource.loader.path", "");
            p.setProperty("file.resource.loader.cache", "false");
            // 初始化Velocity引擎，指定配置Properties
            Velocity.init(p);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
