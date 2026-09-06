package com.ruoyi.file.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.file.domain.SysFileInfo;
import com.ruoyi.file.domain.SysFilePartETag;
import com.ruoyi.file.service.ISysFileInfoService;
import com.ruoyi.file.service.StorageService;
import com.ruoyi.file.utils.FileOperateUtils;
import com.ruoyi.file.utils.StorageUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "默认文件存储")
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private ISysFileInfoService sysFileInfoService;

    /**
     * 获取所有可用存储渠道及其client列表
     */
    @GetMapping("/client-list")
    public AjaxResult getClientList() {
        return AjaxResult.success(StorageUtils.getClientList());
    }

    /**
     * 统一上传接口：/file/{storageType}/{bucketName}/upload
     */
    @PostMapping({ "/upload", "/{storageType}/{bucketName}/upload" })
    public AjaxResult uploadUnified(
            @PathVariable(name = "storageType", required = false) String storageType,
            @PathVariable(name = "bucketName", required = false) String bucketName,
            @RequestParam("file") MultipartFile file) {
        try {
            String filePath = "upload/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            StorageService storageService = null;
            SysFileInfo sysFileInfo = sysFileInfoService.buildSysFileInfo(file);
            if (StringUtils.isEmpty(storageType) || StringUtils.isEmpty(bucketName)) {
                sysFileInfo.setStorageType(StorageUtils.getPrimaryStorageType());
                storageService = new StorageService(StorageUtils.getPrimaryStorageBucket());
            } else {
                sysFileInfo.setStorageType(storageType);
                storageService = new StorageService(StorageUtils.getStorageBucket(storageType, bucketName));
            }
            filePath = storageService.upload(filePath, file);
            String url = storageService.generateUrl(filePath);
            sysFileInfo.setFilePath(filePath);
            sysFileInfoService.insertSysFileInfo(sysFileInfo);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
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
    @GetMapping({ "/download", "/{storageType}/{bucketName}/download" })
    public void downloadUnified(
            @PathVariable(name = "storageType", required = false) String storageType,
            @PathVariable(name = "bucketName", required = false) String bucketName,
            @RequestParam("filePath") String filePath,
            HttpServletResponse response) throws Exception {
        try {
            StorageService storageService = null;
            if (StringUtils.isEmpty(storageType) || StringUtils.isEmpty(bucketName)) {
                storageService = new StorageService(StorageUtils.getPrimaryStorageBucket());
            } else {
                storageService = new StorageService(StorageUtils.getStorageBucket(storageType, bucketName));
            }
            response.setContentType("application/octet-stream");
            storageService.downLoad(filePath, response);
        } catch (Exception e) {
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("下载失败: " + e.getMessage());
        }
    }

    /**
     * 统一预览接口：/file/{storageType}/{bucketName}/preview?filePath=xxx
     */
    @Anonymous
    @GetMapping({ "/preview", "/{storageType}/{bucketName}/preview" })
    public void preview(
            @PathVariable(name = "storageType", required = false) String storageType,
            @PathVariable(name = "bucketName", required = false) String bucketName,
            @RequestParam("filePath") String filePath,
            HttpServletResponse response) throws Exception {
        try {
            StorageService storageService = null;
            if (StringUtils.isEmpty(storageType) || StringUtils.isEmpty(bucketName)) {
                storageService = new StorageService(StorageUtils.getPrimaryStorageBucket());
            } else {
                storageService = new StorageService(StorageUtils.getStorageBucket(storageType, bucketName));
            }
            filePath = URLDecoder.decode(filePath, "UTF-8");
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
            HttpServletResponse response) throws Exception {
        OutputStream outputStream = response.getOutputStream();
        try {
            if (!FileUtils.checkAllowDownload(filePath)) {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", filePath));
            }
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, filePath);
            FileOperateUtils.downLoad(filePath, outputStream);
        } catch (Exception e) {
            response.reset();
            response.setContentType(MediaType.TEXT_HTML_VALUE);
            response.setCharacterEncoding("UTF-8");
            String errorMessage = "下载文件失败: " + e.getMessage();
            outputStream.write(errorMessage.getBytes("UTF-8"));
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
            StorageService fileService = new StorageService(StorageUtils.getPrimaryStorageBucket());
            String uploadId = fileService.initMultipartUpload(objectName, fileSize);
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
        try {
            if (chunk == null || chunk.isEmpty())
                throw new ServiceException("分片数据不能为空");
            StorageService fileService = new StorageService(StorageUtils.getPrimaryStorageBucket());
            String etag = fileService.uploadPart(filePath, uploadId, partNumber, chunk.getSize(),
                    chunk.getInputStream());
            if (etag == null || etag.isEmpty())
                throw new ServiceException("上传分片失败：未获取到ETag");
            return AjaxResult.success(Map.of(
                    "etag", etag,
                    "partNumber", partNumber));
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
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
            if (partETags == null || partETags.isEmpty())
                throw new ServiceException("分片信息不能为空");
            // 验证并排序分片信息
            List<SysFilePartETag> validParts = partETags.stream()
                    .filter(part -> part != null && part.getPartNumber() != null && part.getETag() != null)
                    .peek(part -> {
                        if (part.getPartNumber() <= 0 || StringUtils.isEmpty(part.getETag())) {
                            throw new ServiceException("分片序号或ETag无效");
                        }
                    })
                    .collect(Collectors.toList());
            if (validParts.size() != partETags.size()) {
                throw new ServiceException("分片信息格式不正确");
            }
            validParts.sort(Comparator.comparingInt(p -> p.getPartNumber()));
            // 完成分片上传并合并文件
            StorageService fileService = new StorageService(StorageUtils.getPrimaryStorageBucket());
            String finalPath = fileService.completeMultipartUpload(filePath, uploadId, validParts);
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
            fileInfo.setStorageType(RuoYiConfig.getFileServer());
            fileInfo.setCreateBy(userName);
            fileInfo.setCreateTime(new Date());
            fileInfo.setUpdateBy(userName);
            fileInfo.setUpdateTime(new Date());
            fileInfo.setDelFlag("0");
            sysFileInfoService.insertSysFileInfo(fileInfo);
            return AjaxResult.success(fileInfo);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
