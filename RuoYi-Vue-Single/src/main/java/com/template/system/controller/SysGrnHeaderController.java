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
import com.template.system.entity.SysGrnHeader;
import com.template.system.service.ISysGrnHeaderService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 采购进货单-主Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/grnHeader")
public class SysGrnHeaderController extends BaseController
{
    @Autowired
    private ISysGrnHeaderService sysGrnHeaderService;

    /**
     * 查询采购进货单-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:grnHeader:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysGrnHeader sysGrnHeader)
    {
        startPage();
        List<SysGrnHeader> list = sysGrnHeaderService.selectSysGrnHeaderList(sysGrnHeader);
        return getDataTable(list);
    }

    /**
     * 导出采购进货单-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:grnHeader:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysGrnHeader sysGrnHeader)
    {
        List<SysGrnHeader> list = sysGrnHeaderService.selectSysGrnHeaderList(sysGrnHeader);
        ExcelUtil<SysGrnHeader> util = new ExcelUtil<SysGrnHeader>(SysGrnHeader.class);
        util.exportExcel(response, list, "采购进货单-主数据");
    }

    /**
     * 获取采购进货单-主详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:grnHeader:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysGrnHeaderService.selectSysGrnHeaderById(id));
    }

    /**
     * 新增采购进货单-主
     */
    @PreAuthorize("@ss.hasPermi('system:grnHeader:add')")
    @PostMapping
    public Result add(@RequestBody SysGrnHeader sysGrnHeader)
    {
        return toAjax(sysGrnHeaderService.insertSysGrnHeader(sysGrnHeader));
    }

    /**
     * 修改采购进货单-主
     */
    @PreAuthorize("@ss.hasPermi('system:grnHeader:edit')")
    @PutMapping
    public Result edit(@RequestBody SysGrnHeader sysGrnHeader)
    {
        return toAjax(sysGrnHeaderService.updateSysGrnHeader(sysGrnHeader));
    }

    /**
     * 删除采购进货单-主
     */
    @PreAuthorize("@ss.hasPermi('system:grnHeader:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysGrnHeaderService.deleteSysGrnHeaderByIds(ids));
    }
}
