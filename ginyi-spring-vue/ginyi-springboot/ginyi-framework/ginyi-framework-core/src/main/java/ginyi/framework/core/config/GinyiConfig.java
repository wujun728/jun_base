package ginyi.framework.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author ruoyi
 */
@Component
public class GinyiConfig {

    /**
     * 上传路径
     */
    @Value("${ginyi.profile}")
    public static String profile;

    /**
     * 获取导入上传路径
     */
    public static String getImportPath() {
        return profile + "/import";
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return profile + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return profile + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return profile + "/upload";
    }
}
