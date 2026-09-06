package com.geek.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.EmptyFileException;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.geek.common.config.GeekConfig;
import com.geek.common.core.storage.base.StorageEntity;
import com.geek.common.core.storage.domain.SysFilePartETag;
import com.geek.common.core.storage.service.StorageService;
import com.geek.common.utils.file.FileUtils;
import com.geek.common.utils.file.MimeTypeUtils;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 文件操作工具类
 *
 * @author geek
 */
public class Sb {

    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件路径
     * @throws Exception
     */
    public static String upload(MultipartFile file) {
        return upload(file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
    }

    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件路径
     * @throws Exception
     */
    public static String upload(MultipartFile file, String fileName) {
        return upload(DateUtils.datePath() + File.separator + fileName, file);
    }

    /**
     * 以默认配置进行文件上传
     *
     * @param file             上传的文件
     * @param allowedExtension 允许的扩展名
     * @return 文件路径
     * @throws Exception
     */
    public static String upload(MultipartFile file, String[] allowedExtension) {
        return upload(FileUtils.fastFilePath(file), file, allowedExtension);
    }

    /**
     * 根据文件路径上传
     *
     * @param filePath 上传文件的路径
     * @param file     上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static String upload(String filePath, MultipartFile file) {
        return upload(filePath, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
    }

    /**
     * 根据文件路径上传
     *
     * @param filePath         上传文件的路径
     * @param file             上传的文件
     * @param allowedExtension 允许的扩展名
     * @return 访问链接
     * @throws IOException
     */
    public static String upload(String filePath, MultipartFile file, String[] allowedExtension) {
        try {
            StorageService fileService = new StorageService(GeekConfig.getGeekStorageBucket());
            fileService.setAllowedExtension(allowedExtension);
            return fileService.upload(filePath, file);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 根据文件路径下载
     *
     * @param fileUrl      下载文件路径
     * @param outputStream 需要输出到的输出流
     * @return 文件名称
     * @throws IOException
     */
    public static void downLoad(String filePath, OutputStream outputStream) {
        StorageService fileService = new StorageService(GeekConfig.getGeekStorageBucket());
        try (InputStream inputStream = fileService.downLoad(filePath)) {
            FileUtils.writeBytes(inputStream, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 根据文件路径下载
     *
     * @param filepath 下载文件路径
     * @param response 响应
     * @return 文件名称
     * @throws IOException
     */
    public static void downLoad(String filePath, HttpServletResponse response) {
        StorageService fileService = new StorageService(GeekConfig.getGeekStorageBucket());
        StorageEntity fileEntity;
        try {
            fileEntity = fileService.getFile(filePath);
            InputStream inputStream = fileEntity.getInputStream();
            OutputStream outputStream = response.getOutputStream();
            FileUtils.setAttachmentResponseHeader(response, FileUtils.getName(fileEntity.getFilePath()));
            response.setContentLengthLong(fileEntity.getByteCount());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.writeBytes(inputStream, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 根据文件路径删除
     *
     * @param filePath 下载文件路径
     * @throws IOException
     */
    public static void deleteFile(String filePath) {
        try {
            StorageService fileService = new StorageService(GeekConfig.getGeekStorageBucket());
            fileService.deleteFile(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * 获取文件的访问链接
     *
     * @param filePath 文件路径
     * @return 访问链接
     * @throws Exception
     */
    public static String getURL(String filePath) {
        StorageService fileService = new StorageService(GeekConfig.getGeekStorageBucket());
        try {
            return fileService.generateUrl(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 初始化文件分片上传任务
     *
     * @param filePath 文件路径
     * @param fileSize 文件大小
     * @return 任务ID
     * @throws Exception
     */
    public static String initMultipartUpload(String filePath, Long fileSize) {
        StorageService fileService = new StorageService(GeekConfig.getGeekStorageBucket());
        try {
            return fileService.initMultipartUpload(filePath, fileSize);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 上传文件分片
     *
     * @param filePath   文件路径
     * @param uploadId   上传任务ID
     * @param partNumber 分片序号
     * @param chunk      分片文件
     * @return 分片的ETag
     * @throws Exception
     */
    public static String uploadPart(String filePath, String uploadId, int partNumber, MultipartFile chunk) {
        StorageService fileService = new StorageService(GeekConfig.getGeekStorageBucket());
        try {
            if (chunk == null || chunk.isEmpty())
                throw new EmptyFileException();
            SysFilePartETag partETag = SysFilePartETag.builder()
                    .partNumber(partNumber)
                    .partSize(chunk.getSize())
                    .taskId(uploadId)
                    .filePath(filePath)
                    .build();
            return fileService.uploadPart(partETag, chunk.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 合并文件分片
     *
     * @param filePath  文件路径
     * @param uploadId  上传任务ID
     * @param partETags 分片信息
     * @return 文件地址
     * @throws Exception
     */
    public static String completeMultipartUpload(String filePath, String uploadId, List<SysFilePartETag> partETags) {
        StorageService fileService = new StorageService(GeekConfig.getGeekStorageBucket());
        try {
            return fileService.completeMultipartUpload(filePath, uploadId, partETags);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Sb() {
    }
}
