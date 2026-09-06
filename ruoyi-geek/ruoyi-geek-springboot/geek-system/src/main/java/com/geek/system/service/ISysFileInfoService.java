package com.geek.system.service;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.geek.common.utils.sign.Md5Utils;
import com.geek.system.domain.SysFileInfo;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 文件Service接口
 * 
 * @author geek
 * @date 2025-04-25
 */
public interface ISysFileInfoService extends IService<SysFileInfo> {

    /**
     * 新增文件
     * 
     * @param file
     * @return 结果
     */
    default public SysFileInfo buildSysFileInfo(MultipartFile file) {
        String fileType = null;
        if (file.getOriginalFilename() != null && file.getOriginalFilename().contains(".")) {
            fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        }
        SysFileInfo fileInfo = new SysFileInfo();
        String md5 = Md5Utils.getMd5(file);
        fileInfo.setFileName(file.getOriginalFilename());
        fileInfo.setFileType(fileType);
        fileInfo.setFileSize(file.getSize());
        fileInfo.setMd5(md5);
        fileInfo.setCreateTime(new Date());
        fileInfo.setUpdateTime(new Date());
        fileInfo.setDelFlag(0);
        return fileInfo;
    }

    Page<SysFileInfo> page(SysFileInfo sysFileInfo, int pageNum, int pageSize);

    void export(SysFileInfo sysFileInfo, HttpServletResponse response);
}
