package com.geek.system.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.system.domain.SysFileInfo;
import com.geek.system.mapper.SysFileInfoMapper;
import com.geek.system.service.ISysFileInfoService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 文件Service业务层处理
 * 
 * @author geek
 * @date 2025-04-25
 */
@Service
public class SysFileInfoServiceImpl extends ServiceImpl<SysFileInfoMapper, SysFileInfo> implements ISysFileInfoService {

    /**
     * 查询文件列表
     * 
     * @param sysFileInfo 文件
     * @return 文件
     */
    private QueryChain<SysFileInfo> selectSysFileInfoList(SysFileInfo sysFileInfo) {
        return this.queryChain()
                .from(SysFileInfo.class)
                .like(SysFileInfo::getFileName, sysFileInfo.getFileName())
                .eq(SysFileInfo::getFilePath, sysFileInfo.getFilePath())
                .eq(SysFileInfo::getStorageType, sysFileInfo.getStorageType())
                .eq(SysFileInfo::getFileType, sysFileInfo.getFileType())
                .eq(SysFileInfo::getFileSize, sysFileInfo.getFileSize())
                .eq(SysFileInfo::getMd5, sysFileInfo.getMd5());
    }

    @Override
    public Page<SysFileInfo> page(SysFileInfo sysFileInfo, int pageNum, int pageSize) {
        return this.selectSysFileInfoList(sysFileInfo).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(SysFileInfo sysFileInfo, HttpServletResponse response) {
        List<SysFileInfo> list = this.selectSysFileInfoList(sysFileInfo).list();
        ExcelUtil<SysFileInfo> util = new ExcelUtil<>(SysFileInfo.class);
        util.exportExcel(response, list, "文件数据");
    }
}
