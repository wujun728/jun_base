package com.geek.framework.storage.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.springframework.web.multipart.MultipartFile;

import com.geek.common.core.storage.base.StorageBucket;
import com.geek.common.core.storage.base.StorageEntity;
import com.geek.common.core.storage.domain.SysFilePartETag;
import com.geek.common.core.text.CharsetKit;
import com.geek.common.exception.ServiceException;
import com.geek.common.utils.ServletUtils;
import com.geek.common.utils.sign.Md5Utils;
import com.geek.common.utils.uuid.UUID;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
@Getter
public class LocalBucket implements StorageBucket {

    private String bucketName;
    private String basePath;
    private String permission;
    private String api;

    @Override
    public void put(String filePath, MultipartFile file) {
        Path dest = Paths.get(getBasePath(), filePath);
        try (InputStream inputStream = file.getInputStream()) {
            Files.createDirectories(dest.getParent());
            Files.copy(inputStream, dest);
        } catch (Exception e) {
            throw new ServiceException("Failed to upload file: " + e.getMessage());
        }
    }

    @Override
    public StorageEntity get(String filePath) throws IOException {
        Path file = Paths.get(getBasePath(), filePath);
        StorageEntity fileEntity = new StorageEntity();
        fileEntity.setFilePath(filePath);
        fileEntity.setInputStream(new FileInputStream(file.toFile()));
        fileEntity.setByteCount(file.toFile().length());
        return fileEntity;
    }

    @Override
    public void remove(String filePath) throws IOException {
        Path file = Paths.get(getBasePath(), filePath);
        Files.deleteIfExists(file);
    }

    @Override
    public URL generatePresignedUrl(String filePath, int expireTime) throws Exception {
        HttpServletRequest request = ServletUtils.getRequest();
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getSession().getServletContext().getContextPath();
        String toHex = Md5Utils.hash(filePath + expireTime);
        StringBuilder sb = new StringBuilder();
        sb.append(url.delete(url.length() - request.getRequestURI().length(), url.length())
                .append(contextPath).toString())
                .append(getApi()).append("?")
                .append("filePath=").append(URLEncoder.encode(filePath, CharsetKit.UTF_8))
                .append("&toHex=").append(toHex);
        return URI.create(sb.toString()).toURL();
    }

    @Override
    public URL generatePublicUrl(String filePath) throws Exception {
        HttpServletRequest request = ServletUtils.getRequest();
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getSession().getServletContext().getContextPath();
        StringBuilder sb = new StringBuilder();
        sb.append(url.delete(url.length() - request.getRequestURI().length(), url.length())
                .append(contextPath).toString()).append(getApi())
                .append("/").append(filePath.replace("\\", "/"));
        return new URI(sb.toString()).toURL();
    }

    public String initMultipartUpload(String filePath) throws Exception {
        String uploadId = UUID.randomUUID().toString();
        Path tempDir = getMultipartTempDir(uploadId);
        try {
            Files.createDirectories(tempDir);
            saveUploadMetadata(uploadId, filePath);
            return uploadId;
        } catch (Exception e) {
            throw new ServiceException("初始化分片上传失败: " + e.getMessage());
        }
    }

    public SysFilePartETag uploadPart(String filePath, String uploadId, int partNumber, long partSize,
            InputStream inputStream)
            throws Exception {
        validateUploadMetadata(uploadId, filePath);
        Path partPath = getPartPath(uploadId, partNumber);
        try (OutputStream fos = Files.newOutputStream(partPath)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            long totalBytesWritten = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1 && totalBytesWritten < partSize) {
                int writeSize = (int) Math.min(bytesRead, partSize - totalBytesWritten);
                fos.write(buffer, 0, writeSize);
                totalBytesWritten += writeSize;
            }
            if (totalBytesWritten != partSize) {
                throw new ServiceException("分片大小不匹配: 预期=" + partSize + ", 实际=" + totalBytesWritten);
            }
        }
        String etag = Md5Utils.getMd5(partPath.toFile());
        if (etag == null) {
            throw new ServiceException("计算分片 MD5 失败");
        }
        etag = etag.toUpperCase();
        return SysFilePartETag.builder()
                .partNumber(partNumber)
                .eTag(etag)
                .partSize(partSize)
                .build();
    }

    public String completeMultipartUpload(String filePath, String uploadId, List<SysFilePartETag> partETags)
            throws Exception {
        validateUploadMetadata(uploadId, filePath);
        List<SysFilePartETag> sortedParts = partETags.stream()
                .sorted(Comparator.comparingInt(SysFilePartETag::getPartNumber))
                .toList();
        if (sortedParts.isEmpty()) {
            throw new ServiceException("分片列表不能为空");
        }
        for (SysFilePartETag partETag : sortedParts) {
            Path partPath = getPartPath(uploadId, partETag.getPartNumber());
            if (!Files.exists(partPath)) {
                throw new ServiceException("分片不存在: 序号=" + partETag.getPartNumber());
            }
            String actualEtag = Md5Utils.getMd5(partPath.toFile());
            if (actualEtag == null || !actualEtag.toUpperCase().equals(partETag.getETag())) {
                throw new ServiceException("分片验证失败: 序号=" + partETag.getPartNumber());
            }
        }
        Path destPath = Paths.get(getBasePath(), filePath);
        Files.createDirectories(destPath.getParent());
        try (WritableByteChannel outChannel = Files.newByteChannel(
                destPath,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            for (SysFilePartETag part : sortedParts) {
                Path partPath = getPartPath(uploadId, part.getPartNumber());
                try (FileChannel inChannel = FileChannel.open(partPath, StandardOpenOption.READ)) {
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                }
            }
        }
        deleteTempDir(uploadId);
        return filePath;
    }

    private static final String MULTIPART_DIR = "temp_uploads";
    private static final String MULTIPART_META_FILE = "upload.properties";

    private Path getMultipartTempDir(String uploadId) {
        return Paths.get(getBasePath(), MULTIPART_DIR, uploadId);
    }

    private Path getMultipartMetadataPath(String uploadId) {
        return getMultipartTempDir(uploadId).resolve(MULTIPART_META_FILE);
    }

    private Path getPartPath(String uploadId, int partNumber) {
        return getMultipartTempDir(uploadId).resolve("part_" + partNumber);
    }

    private void saveUploadMetadata(String uploadId, String filePath) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("filePath", filePath);
        properties.setProperty("createdAt", String.valueOf(System.currentTimeMillis()));
        try (OutputStream outputStream = Files.newOutputStream(
                getMultipartMetadataPath(uploadId),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE)) {
            properties.store(outputStream, null);
        }
    }

    private void validateUploadMetadata(String uploadId, String filePath) throws IOException {
        Path tempDir = getMultipartTempDir(uploadId);
        if (!Files.isDirectory(tempDir)) {
            throw new ServiceException("无效的 uploadId: " + uploadId);
        }
        Properties properties = new Properties();
        Path metadataPath = getMultipartMetadataPath(uploadId);
        if (!Files.exists(metadataPath)) {
            throw new ServiceException("上传元数据不存在: " + uploadId);
        }
        try (InputStream inputStream = Files.newInputStream(metadataPath, StandardOpenOption.READ)) {
            properties.load(inputStream);
        }
        String storedFilePath = properties.getProperty("filePath");
        if (storedFilePath == null || !storedFilePath.equals(filePath)) {
            throw new ServiceException("上传任务与文件路径不匹配: " + uploadId);
        }
    }

    private void deleteTempDir(String uploadId) throws IOException {
        Path tempDir = getMultipartTempDir(uploadId);
        try (var walk = Files.walk(tempDir)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    public String getBasePath() {
        if (basePath != null && !basePath.endsWith("/")) {
            basePath = basePath + "/";
        }
        return basePath;
    }
}
