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
import com.template.system.entity.SysTransferLine;
import com.template.system.service.ISysTransferLineService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 调拨单-明细Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/transferLine")
public class SysTransferLineController extends BaseController
{
    @Autowired
    private ISysTransferLineService sysTransferLineService;

    /**
     * 查询调拨单-明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:transferLine:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysTransferLine sysTransferLine)
    {
        startPage();
        List<SysTransferLine> list = sysTransferLineService.selectSysTransferLineList(sysTransferLine);
        return getDataTable(list);
    }

    /**
     * 导出调拨单-明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:transferLine:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysTransferLine sysTransferLine)
    {
        List<SysTransferLine> list = sysTransferLineService.selectSysTransferLineList(sysTransferLine);
        ExcelUtil<SysTransferLine> util = new ExcelUtil<SysTransferLine>(SysTransferLine.class);
        util.exportExcel(response, list, "调拨单-明细数据");
    }

    /**
     * 获取调拨单-明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:transferLine:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysTransferLineService.selectSysTransferLineById(id));
    }

    /**
     * 新增调拨单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:transferLine:add')")
    @PostMapping
    public Result add(@RequestBody SysTransferLine sysTransferLine)
    {
        return toAjax(sysTransferLineService.insertSysTransferLine(sysTransferLine));
    }

    /**
     * 修改调拨单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:transferLine:edit')")
    @PutMapping
    public Result edit(@RequestBody SysTransferLine sysTransferLine)
    {
        return toAjax(sysTransferLineService.updateSysTransferLine(sysTransferLine));
    }

    /**
     * 删除调拨单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:transferLine:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysTransferLineService.deleteSysTransferLineByIds(ids));
    }
}
