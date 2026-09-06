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
import com.template.system.entity.SysStockCountHeader;
import com.template.system.service.ISysStockCountHeaderService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 盘点单-主Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/stockCountHeader")
public class SysStockCountHeaderController extends BaseController
{
    @Autowired
    private ISysStockCountHeaderService sysStockCountHeaderService;

    /**
     * 查询盘点单-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:stockCountHeader:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysStockCountHeader sysStockCountHeader)
    {
        startPage();
        List<SysStockCountHeader> list = sysStockCountHeaderService.selectSysStockCountHeaderList(sysStockCountHeader);
        return getDataTable(list);
    }

    /**
     * 导出盘点单-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:stockCountHeader:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysStockCountHeader sysStockCountHeader)
    {
        List<SysStockCountHeader> list = sysStockCountHeaderService.selectSysStockCountHeaderList(sysStockCountHeader);
        ExcelUtil<SysStockCountHeader> util = new ExcelUtil<SysStockCountHeader>(SysStockCountHeader.class);
        util.exportExcel(response, list, "盘点单-主数据");
    }

    /**
     * 获取盘点单-主详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:stockCountHeader:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysStockCountHeaderService.selectSysStockCountHeaderById(id));
    }

    /**
     * 新增盘点单-主
     */
    @PreAuthorize("@ss.hasPermi('system:stockCountHeader:add')")
    @PostMapping
    public Result add(@RequestBody SysStockCountHeader sysStockCountHeader)
    {
        return toAjax(sysStockCountHeaderService.insertSysStockCountHeader(sysStockCountHeader));
    }

    /**
     * 修改盘点单-主
     */
    @PreAuthorize("@ss.hasPermi('system:stockCountHeader:edit')")
    @PutMapping
    public Result edit(@RequestBody SysStockCountHeader sysStockCountHeader)
    {
        return toAjax(sysStockCountHeaderService.updateSysStockCountHeader(sysStockCountHeader));
    }

    /**
     * 删除盘点单-主
     */
    @PreAuthorize("@ss.hasPermi('system:stockCountHeader:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysStockCountHeaderService.deleteSysStockCountHeaderByIds(ids));
    }
}
