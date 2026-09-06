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
import com.template.system.entity.SysPoHeader;
import com.template.system.service.ISysPoHeaderService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 采购订单-主Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/poHeader")
public class SysPoHeaderController extends BaseController
{
    @Autowired
    private ISysPoHeaderService sysPoHeaderService;

    /**
     * 查询采购订单-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:poHeader:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPoHeader sysPoHeader)
    {
        startPage();
        List<SysPoHeader> list = sysPoHeaderService.selectSysPoHeaderList(sysPoHeader);
        return getDataTable(list);
    }

    /**
     * 导出采购订单-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:poHeader:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPoHeader sysPoHeader)
    {
        List<SysPoHeader> list = sysPoHeaderService.selectSysPoHeaderList(sysPoHeader);
        ExcelUtil<SysPoHeader> util = new ExcelUtil<SysPoHeader>(SysPoHeader.class);
        util.exportExcel(response, list, "采购订单-主数据");
    }

    /**
     * 获取采购订单-主详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:poHeader:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysPoHeaderService.selectSysPoHeaderById(id));
    }

    /**
     * 新增采购订单-主
     */
    @PreAuthorize("@ss.hasPermi('system:poHeader:add')")
    @PostMapping
    public Result add(@RequestBody SysPoHeader sysPoHeader)
    {
        return toAjax(sysPoHeaderService.insertSysPoHeader(sysPoHeader));
    }

    /**
     * 修改采购订单-主
     */
    @PreAuthorize("@ss.hasPermi('system:poHeader:edit')")
    @PutMapping
    public Result edit(@RequestBody SysPoHeader sysPoHeader)
    {
        return toAjax(sysPoHeaderService.updateSysPoHeader(sysPoHeader));
    }

    /**
     * 删除采购订单-主
     */
    @PreAuthorize("@ss.hasPermi('system:poHeader:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysPoHeaderService.deleteSysPoHeaderByIds(ids));
    }
}
