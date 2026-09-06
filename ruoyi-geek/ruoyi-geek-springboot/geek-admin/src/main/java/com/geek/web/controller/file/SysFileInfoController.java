package com.geek.web.controller.file;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geek.common.annotation.Log;
import com.geek.common.config.GeekConfig;
import com.geek.common.core.controller.BaseController;
import com.geek.common.core.domain.AjaxResult;
import com.geek.common.core.page.PageDomain;
import com.geek.common.core.page.TableDataInfo;
import com.geek.common.core.page.TableSupport;
import com.geek.common.core.storage.service.StorageService;
import com.geek.common.enums.BusinessType;
import com.geek.system.domain.SysFileInfo;
import com.geek.system.service.ISysFileInfoService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 文件Controller
 * 
 * @author geek
 * @date 2025-04-25
 */
@RestController
@RequestMapping("/file/info")
@Tag(name = "【文件】管理")
public class SysFileInfoController extends BaseController {
    @Autowired
    private ISysFileInfoService sysFileInfoService;

    /**
     * 查询文件列表
     */
    @Operation(summary = "查询文件列表")
    @PreAuthorize("@ss.hasPermi('system:file:list')")
    @GetMapping("/list")
    public TableDataInfo<SysFileInfo> list(SysFileInfo sysFileInfo) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<SysFileInfo> list = sysFileInfoService.page(sysFileInfo, pageDomain.getPageNum(),
                pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出文件列表
     */
    @Operation(summary = "导出文件列表")
    @PreAuthorize("@ss.hasPermi('system:file:export')")
    @Log(title = "文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysFileInfo sysFileInfo) {
        sysFileInfoService.export(sysFileInfo, response);
    }

    /**
     * 获取文件详细信息
     */
    @Operation(summary = "获取文件详细信息")
    @PreAuthorize("@ss.hasPermi('system:file:query')")
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@PathVariable("fileId") Long fileId) {
        return success(sysFileInfoService.getById(fileId));
    }

    /**
     * 新增文件
     */
    @Operation(summary = "新增文件")
    @PreAuthorize("@ss.hasPermi('system:file:add')")
    @Log(title = "文件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysFileInfo sysFileInfo) {
        return toAjax(sysFileInfoService.save(sysFileInfo));
    }

    /**
     * 修改文件
     */
    @Operation(summary = "修改文件")
    @PreAuthorize("@ss.hasPermi('system:file:edit')")
    @Log(title = "文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysFileInfo sysFileInfo) {
        return toAjax(sysFileInfoService.updateById(sysFileInfo));
    }

    /**
     * 删除文件
     */
    @Operation(summary = "删除文件")
    @PreAuthorize("@ss.hasPermi('system:file:remove')")
    @Log(title = "文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable(name = "fileIds") List<Long> fileIds) {
        StorageService fileService = new StorageService(GeekConfig.getGeekStorageBucket());
        sysFileInfoService.list(QueryWrapper.create().in(SysFileInfo::getFileId, fileIds))
            .stream()
            .map(SysFileInfo::getFilePath)
            .forEach(fileService::clearFileCache);
        return toAjax(sysFileInfoService.removeByIds(fileIds));
    }
}
