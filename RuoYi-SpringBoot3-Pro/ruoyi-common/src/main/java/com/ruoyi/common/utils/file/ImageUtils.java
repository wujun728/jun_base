package com.ruoyi.common.utils.file;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import cn.hutool.core.io.FileUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.StringUtils;

/**
 * 图片处理工具类
 *
 * @author ruoyi
 */
public class ImageUtils {
    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

    /**
     * 生成图片缩略图
     * 此方法允许根据指定的宽度、高度、缩放比例和质量来生成图片的缩略图
     * 如果提供了宽度和高度，则使用指定尺寸生成缩略图；如果提供了缩放比例，则按该比例缩放原图生成缩略图
     * 可以选择性地调整输出图片的质量
     *
     * @param inputImg  输入图片的路径
     * @param outputImg 输出缩略图的路径
     * @param width     缩略图的宽度，单位为像素如果不需要指定宽度，请设置为0
     * @param height    缩略图的高度，单位为像素如果不需要指定高度，请设置为0
     * @param scale     缩略图的缩放比例，相对于原图如果不需要缩放，请设置为0
     * @param quality   缩略图的质量，取值范围为0.0到1.0之间，值越大表示质量越高如果不需要调整质量，请设置为0
     */
    public static void genThumbnails(String inputImg, String outputImg, int width, int height, float scale, float quality) {
        try {
            // 创建缩略图生成器
            Thumbnails.Builder builder = Thumbnails.of(FileUtil.file(inputImg));
            // 根据提供的宽度和高度设置缩略图尺寸
            if (width > 0 && height > 0) {
                builder.size(width, height);
            } else if (scale > 0) {
                // 如果未提供尺寸但提供了缩放比例，则按比例缩放原图
                builder.scale(scale);
            }
            if (quality > 0) {
                // 设置缩略图的质量
                builder.outputQuality(quality);
            }
            // 将生成的缩略图保存到指定路径
            builder.toFile(outputImg);
        } catch (IOException e) {
            // 如果在处理图片时发生IO异常，则抛出运行时异常
            throw new RuntimeException(e);
        }
    }


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
        InputStream in = null;
        try {
            if (url.startsWith("http")) {
                // 网络地址
                URL urlObj = new URL(url);
                URLConnection urlConnection = urlObj.openConnection();
                urlConnection.setConnectTimeout(30 * 1000);
                urlConnection.setReadTimeout(60 * 1000);
                urlConnection.setDoInput(true);
                in = urlConnection.getInputStream();
            } else {
                // 本机地址
                String localPath = RuoYiConfig.getProfile();
                String downloadPath = localPath + StringUtils.substringAfter(url, Constants.RESOURCE_PREFIX);
                in = new FileInputStream(downloadPath);
            }
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            log.error("获取文件路径异常 {}", e);
            return null;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
