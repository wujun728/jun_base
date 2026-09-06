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
import com.template.system.entity.SysGrnLine;
import com.template.system.service.ISysGrnLineService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 采购进货单-明细Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/grnLine")
public class SysGrnLineController extends BaseController
{
    @Autowired
    private ISysGrnLineService sysGrnLineService;

    /**
     * 查询采购进货单-明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:grnLine:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysGrnLine sysGrnLine)
    {
        startPage();
        List<SysGrnLine> list = sysGrnLineService.selectSysGrnLineList(sysGrnLine);
        return getDataTable(list);
    }

    /**
     * 导出采购进货单-明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:grnLine:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysGrnLine sysGrnLine)
    {
        List<SysGrnLine> list = sysGrnLineService.selectSysGrnLineList(sysGrnLine);
        ExcelUtil<SysGrnLine> util = new ExcelUtil<SysGrnLine>(SysGrnLine.class);
        util.exportExcel(response, list, "采购进货单-明细数据");
    }

    /**
     * 获取采购进货单-明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:grnLine:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysGrnLineService.selectSysGrnLineById(id));
    }

    /**
     * 新增采购进货单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:grnLine:add')")
    @PostMapping
    public Result add(@RequestBody SysGrnLine sysGrnLine)
    {
        return toAjax(sysGrnLineService.insertSysGrnLine(sysGrnLine));
    }

    /**
     * 修改采购进货单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:grnLine:edit')")
    @PutMapping
    public Result edit(@RequestBody SysGrnLine sysGrnLine)
    {
        return toAjax(sysGrnLineService.updateSysGrnLine(sysGrnLine));
    }

    /**
     * 删除采购进货单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:grnLine:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysGrnLineService.deleteSysGrnLineByIds(ids));
    }
}
