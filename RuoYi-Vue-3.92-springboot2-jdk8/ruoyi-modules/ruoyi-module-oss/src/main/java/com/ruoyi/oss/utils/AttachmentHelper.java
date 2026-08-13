package com.ruoyi.oss.utils;

import com.ruoyi.oss.OSSUploadHelper;
import com.ruoyi.oss.config.OssConfig;
import com.ruoyi.oss.exception.FileNameLengthLimitExceededException;
import com.ruoyi.oss.exception.InvalidExtensionException;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.oss.controller
 * @title: 附件上传助手
 * @description: 附件上传助手 * @date: 2018-04-25 14:25:55
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */
@Component("attachmentHelper")
@EnableConfigurationProperties({OssConfig.class})
public class AttachmentHelper {

    @Autowired
    private OssConfig ossConfig;

    private OSSUploadHelper uploadHelper;

    @PostConstruct
    public void initHelper() {
        try {
            uploadHelper = new OSSUploadHelper();
            uploadHelper.init(ossConfig);
        } catch (Exception e) {
            // OSS配置未就绪时跳过，首次文件上传时再初始化
        }
    }

    public String upload(HttpServletRequest request, MultipartFile file) throws FileSizeLimitExceededException,
            IOException, FileNameLengthLimitExceededException, InvalidExtensionException {
        String basePath = request.getParameter("directory");
        return upload(request, file, basePath);
    }

    public String upload(HttpServletRequest request, MultipartFile file, String directory) throws FileSizeLimitExceededException,
            IOException, FileNameLengthLimitExceededException, InvalidExtensionException {
        String url = uploadHelper.upload(request, file, directory);
        return url;
    }

    public String remote(HttpServletRequest request, String remoteUrl) throws FileSizeLimitExceededException,
            IOException, FileNameLengthLimitExceededException, InvalidExtensionException {
        String basePath = request.getParameter("base_path");
        String url = uploadHelper.remote(request, remoteUrl, basePath);
        return url;
    }

}
