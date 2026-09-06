package com.geek.common.utils.file;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geek.common.config.GeekConfig;
import com.geek.common.constant.Constants;
import com.geek.common.utils.StringUtils;

/**
 * 图片处理工具类
 *
 * @author geek
 */
public class ImageUtils {
    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

    public static byte[] getImage(String imagePath) {
        InputStream is = getFile(imagePath);
        try {
            return IOUtils.toByteArray(is);
        } catch (Exception e) {
            log.error("图片加载异常 {}", e);
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public static InputStream getFile(String imagePath) {
        try {
            byte[] result = readFile(imagePath);
            if (result == null) {
                return null;
            }
            result = Arrays.copyOf(result, result.length);
            return new ByteArrayInputStream(result);
        } catch (Exception e) {
            log.error("获取图片异常 {}", e);
        }
        return null;
    }

    /**
     * 读取文件为字节数据
     *
     * @param url 地址
     * @return 字节数据
     */
    public static byte[] readFile(String url) {
        try (InputStream in = getInputStream(url)) {
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            log.error("获取文件路径异常 {}", e);
            return null;
        }
    }

    private static InputStream getInputStream(String url) throws IOException, URISyntaxException {
        if (url.startsWith("http")) {
            // 网络地址
            URI uriObj = new URI(url);
            URL urlObj = uriObj.toURL();
            URLConnection urlConnection = urlObj.openConnection();
            urlConnection.setConnectTimeout(30 * 1000);
            urlConnection.setReadTimeout(60 * 1000);
            urlConnection.setDoInput(true);
            return urlConnection.getInputStream();
        } else {
            // 本机地址
            String localPath = GeekConfig.getProfile();
            String downloadPath = localPath + StringUtils.substringAfter(url, Constants.RESOURCE_PREFIX);
            return new FileInputStream(downloadPath);
        }
    }

    private ImageUtils() {
    }
}
