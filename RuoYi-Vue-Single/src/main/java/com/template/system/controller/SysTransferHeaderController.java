package com.template.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.template.framework.core.controller.BaseController;
import com.template.framework.core.domain.Result;
import com.template.system.entity.SysTransferHeader;
import com.template.system.service.ISysTransferHeaderService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 调拨单-主Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/transferHeader")
public class SysTransferHeaderController extends BaseController
{
    @Autowired
    private ISysTransferHeaderService sysTransferHeaderService;

    /**
     * 查询调拨单-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:transferHeader:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysTransferHeader sysTransferHeader)
    {
        startPage();
        List<SysTransferHeader> list = sysTransferHeaderService.selectSysTransferHeaderList(sysTransferHeader);
        return getDataTable(list);
    }

    /**
     * 导出调拨单-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:transferHeader:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysTransferHeader sysTransferHeader)
    {
        List<SysTransferHeader> list = sysTransferHeaderService.selectSysTransferHeaderList(sysTransferHeader);
        ExcelUtil<SysTransferHeader> util = new ExcelUtil<SysTransferHeader>(SysTransferHeader.class);
        util.exportExcel(response, list, "调拨单-主数据");
    }

    /**
     * 获取调拨单-主详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:transferHeader:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysTransferHeaderService.selectSysTransferHeaderById(id));
    }

    /**
     * 新增调拨单-主
     */
    @PreAuthorize("@ss.hasPermi('system:transferHeader:add')")
    @PostMapping
    public Result add(@RequestBody SysTransferHeader sysTransferHeader)
    {
        return toAjax(sysTransferHeaderService.insertSysTransferHeader(sysTransferHeader));
    }

    /**
     * 修改调拨单-主
     */
    @PreAuthorize("@ss.hasPermi('system:transferHeader:edit')")
    @PutMapping
    public Result edit(@RequestBody SysTransferHeader sysTransferHeader)
    {
        return toAjax(sysTransferHeaderService.updateSysTransferHeader(sysTransferHeader));
    }

    /**
     * 删除调拨单-主
     */
    @PreAuthorize("@ss.hasPermi('system:transferHeader:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysTransferHeaderService.deleteSysTransferHeaderByIds(ids));
    }
}
