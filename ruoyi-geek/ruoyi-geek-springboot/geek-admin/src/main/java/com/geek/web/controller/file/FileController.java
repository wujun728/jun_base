package com.geek.web.controller.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.geek.common.annotation.Anonymous;
import com.geek.common.config.GeekConfig;
import com.geek.common.core.controller.BaseController;
import com.geek.common.core.domain.AjaxResult;
import com.geek.common.core.storage.GeekStorageBucket;
import com.geek.common.core.storage.StorageBucketKey;
import com.geek.common.core.storage.domain.SysFilePartETag;
import com.geek.common.core.storage.service.StorageService;
import com.geek.common.core.text.CharsetKit;
import com.geek.common.exception.ServiceException;
import com.geek.common.utils.Sb;
import com.geek.common.utils.SecurityUtils;
import com.geek.common.utils.StringUtils;
import com.geek.common.utils.file.FileUtils;
import com.geek.system.domain.SysFileInfo;
import com.geek.system.service.ISysFileInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "默认文件存储")
@RestController
@RequestMapping("/file")
@Anonymous
public class FileController extends BaseController {

    @Autowired
    private ISysFileInfoService sysFileInfoService;

    /**
     * 获取所有可用存储渠道及其client列表
     */
    @GetMapping("/client-list")
    public AjaxResult getClientList() {
        return success(GeekConfig.getGeekStorageBucket().getStorageBucketMap().keySet());
    }

    /**
     * 统一上传接口：/file/{bucketName}/upload
     */
    @PostMapping({ "/upload", "/{bucketName}/upload" })
    public AjaxResult uploadUnified(
            @PathVariable(name = "bucketName", required = false) String bucketName,
            @RequestParam("file") MultipartFile file) {
        try {
            GeekStorageBucket geekStorageBucket = GeekConfig.getGeekStorageBucket();
            String filePath = "upload/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            SysFileInfo sysFileInfo = sysFileInfoService.buildSysFileInfo(file);
            AjaxResult ajax = AjaxResult.success();
            if (StringUtils.isEmpty(bucketName)) {
                sysFileInfo.setStorageType(geekStorageBucket.getDefaultSbType());
                sysFileInfo.setFilePath(Sb.upload(filePath, file));
                ajax.put("url", Sb.getURL(filePath));
            } else {
                sysFileInfo.setStorageType(geekStorageBucket.getSbType(bucketName));
                try {
                    StorageBucketKey.use(bucketName);
                    sysFileInfo.setFilePath(Sb.upload(filePath, file));
                    ajax.put("url", Sb.getURL(filePath));
                } finally {
                    StorageBucketKey.clear();
                }
            }
            sysFileInfoService.save(sysFileInfo);
            ajax.put("info", sysFileInfo);
            ajax.put("fileName", sysFileInfo.getFileName());
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 统一下载接口：/file/{storageType}/{bucketName}/download?filePath=xxx
     */
    @GetMapping({ "/download", "/{bucketName}/download" })
    public void downloadUnified(
            @PathVariable(name = "bucketName", required = false) String bucketName,
            @RequestParam("filePath") String filePath,
            HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/octet-stream");
            if (StringUtils.isEmpty(bucketName)) {
                Sb.downLoad(filePath, response);
            } else {
                StorageBucketKey.use(bucketName, () -> Sb.downLoad(filePath, response));
            }
        } catch (Exception e) {
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("下载失败: " + e.getMessage());
        }
    }

    /**
     * 统一预览接口：/file/{storageType}/{bucketName}/preview?filePath=xxx
     */
    @Anonymous
    @GetMapping({ "/preview", "/{bucketName}/preview" })
    public void preview(
            @PathVariable(name = "storageType", required = false) String storageType,
            @PathVariable(name = "bucketName", required = false) String bucketName,
            @RequestParam("filePath") String filePath,
            HttpServletResponse response) throws Exception {
        try {
            if (StringUtils.isEmpty(bucketName)) {
                StorageBucketKey.use(bucketName);
            }
            StorageService storageService = new StorageService(GeekConfig.getGeekStorageBucket());
            filePath = URLDecoder.decode(filePath, CharsetKit.UTF_8);
            InputStream inputStream = storageService.downLoad(filePath);
            String contentType = URLConnection.guessContentTypeFromName(FileUtils.getName(filePath));
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            response.setContentType(contentType);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("预览失败: " + e.getMessage());
        } finally {
            if(StorageBucketKey.get() != null){
                StorageBucketKey.clear();
            }
        }
    }

    /**
     * 本地资源通用下载
     */
    @Operation(summary = "本地资源通用下载")
    @GetMapping("/resource")
    @Anonymous
    public void resourceDownload(
            @RequestParam String filePath,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        OutputStream outputStream = response.getOutputStream();
        try {
            if (!FileUtils.checkAllowDownload(filePath)) {
                throw new IllegalArgumentException(StringUtils.format("资源文件({})非法，不允许下载。 ", filePath));
            }
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, filePath);
            Sb.downLoad(filePath, outputStream);
        } catch (Exception e) {
            response.reset();
            response.setContentType(MediaType.TEXT_HTML_VALUE);
            response.setCharacterEncoding(CharsetKit.UTF_8);
            String errorMessage = "下载文件失败: " + e.getMessage();
            outputStream.write(errorMessage.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } finally {
            outputStream.close();
        }
    }

    /**
     * 初始化分片上传
     */
    @PostMapping("/initUpload")
    public AjaxResult initMultipartUpload(
            @RequestParam("fileName") String fileName,
            @RequestParam("fileSize") Long fileSize) {
        try {
            if (fileName == null || fileName.isEmpty() || fileSize == null || fileSize <= 0) {
                throw new ServiceException("文件名或文件大小不能为空");
            }
            String currentDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String timestamp = String.valueOf(System.currentTimeMillis());
            String objectName = String.format("%s/%s/%s_%s", "/upload", currentDate, timestamp, fileName);
            String uploadId = Sb.initMultipartUpload(objectName, fileSize);
            return AjaxResult.success(Map.of(
                    "uploadId", uploadId,
                    "filePath", objectName,
                    "fileName", fileName));
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 上传文件分片
     */
    @PostMapping("/uploadChunk")
    public AjaxResult uploadFileChunk(
            @RequestParam("uploadId") String uploadId,
            @RequestParam("filePath") String filePath,
            @RequestParam("partNumber") int partNumber,
            @RequestParam("chunk") MultipartFile chunk) {
        if (chunk == null || chunk.isEmpty())
            throw new ServiceException("分片数据不能为空");
        String etag = Sb.uploadPart(filePath, uploadId, partNumber, chunk);
        if (etag == null || etag.isEmpty())
            throw new ServiceException("上传分片失败：未获取到ETag");
        return AjaxResult.success(Map.of(
                "etag", etag,
                "partNumber", partNumber));
    }

    /**
     * 完成分片上传并合并文件
     */
    @PostMapping("/completeUpload")
    public AjaxResult completeMultipartUpload(
            @RequestParam("uploadId") String uploadId,
            @RequestParam("filePath") String filePath,
            @RequestParam("fileSize") Long fileSize,
            @RequestParam("fileName") String fileName,
            @RequestBody List<SysFilePartETag> partETags) {
        try {
            // 完成分片上传并合并文件
            String finalPath = Sb.completeMultipartUpload(filePath, uploadId, partETags);
            if (finalPath == null || finalPath.isEmpty()) {
                throw new ServiceException("合并分片失败：未获取到最终文件路径");
            }
            // 创建文件记录
            int dotIndex = fileName.lastIndexOf('.');
            String userName = SecurityUtils.getUsername();
            SysFileInfo fileInfo = new SysFileInfo();
            fileInfo.setFileName(fileName);
            fileInfo.setFilePath(finalPath);
            fileInfo.setFileSize(fileSize);
            fileInfo.setFileType(dotIndex >= 0 ? fileName.substring(dotIndex + 1) : "");
            fileInfo.setStorageType(GeekConfig.getGeekStorageBucket().getDefaultSbType());
            fileInfo.setCreateBy(userName);
            fileInfo.setCreateTime(new Date());
            fileInfo.setUpdateBy(userName);
            fileInfo.setUpdateTime(new Date());
            fileInfo.setDelFlag(0);
            sysFileInfoService.save(fileInfo);
            return AjaxResult.success(fileInfo);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
