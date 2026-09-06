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
import com.template.system.entity.SysReturnHeader;
import com.template.system.service.ISysReturnHeaderService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 退库单-主Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/returnHeader")
public class SysReturnHeaderController extends BaseController
{
    @Autowired
    private ISysReturnHeaderService sysReturnHeaderService;

    /**
     * 查询退库单-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:returnHeader:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysReturnHeader sysReturnHeader)
    {
        startPage();
        List<SysReturnHeader> list = sysReturnHeaderService.selectSysReturnHeaderList(sysReturnHeader);
        return getDataTable(list);
    }

    /**
     * 导出退库单-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:returnHeader:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysReturnHeader sysReturnHeader)
    {
        List<SysReturnHeader> list = sysReturnHeaderService.selectSysReturnHeaderList(sysReturnHeader);
        ExcelUtil<SysReturnHeader> util = new ExcelUtil<SysReturnHeader>(SysReturnHeader.class);
        util.exportExcel(response, list, "退库单-主数据");
    }

    /**
     * 获取退库单-主详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:returnHeader:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysReturnHeaderService.selectSysReturnHeaderById(id));
    }

    /**
     * 新增退库单-主
     */
    @PreAuthorize("@ss.hasPermi('system:returnHeader:add')")
    @PostMapping
    public Result add(@RequestBody SysReturnHeader sysReturnHeader)
    {
        return toAjax(sysReturnHeaderService.insertSysReturnHeader(sysReturnHeader));
    }

    /**
     * 修改退库单-主
     */
    @PreAuthorize("@ss.hasPermi('system:returnHeader:edit')")
    @PutMapping
    public Result edit(@RequestBody SysReturnHeader sysReturnHeader)
    {
        return toAjax(sysReturnHeaderService.updateSysReturnHeader(sysReturnHeader));
    }

    /**
     * 删除退库单-主
     */
    @PreAuthorize("@ss.hasPermi('system:returnHeader:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysReturnHeaderService.deleteSysReturnHeaderByIds(ids));
    }
}
