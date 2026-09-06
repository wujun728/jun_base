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
import com.template.system.entity.SysPoLine;
import com.template.system.service.ISysPoLineService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 采购订单-明细Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/poLine")
public class SysPoLineController extends BaseController
{
    @Autowired
    private ISysPoLineService sysPoLineService;

    /**
     * 查询采购订单-明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:poLine:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPoLine sysPoLine)
    {
        startPage();
        List<SysPoLine> list = sysPoLineService.selectSysPoLineList(sysPoLine);
        return getDataTable(list);
    }

    /**
     * 导出采购订单-明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:poLine:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPoLine sysPoLine)
    {
        List<SysPoLine> list = sysPoLineService.selectSysPoLineList(sysPoLine);
        ExcelUtil<SysPoLine> util = new ExcelUtil<SysPoLine>(SysPoLine.class);
        util.exportExcel(response, list, "采购订单-明细数据");
    }

    /**
     * 获取采购订单-明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:poLine:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysPoLineService.selectSysPoLineById(id));
    }

    /**
     * 新增采购订单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:poLine:add')")
    @PostMapping
    public Result add(@RequestBody SysPoLine sysPoLine)
    {
        return toAjax(sysPoLineService.insertSysPoLine(sysPoLine));
    }

    /**
     * 修改采购订单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:poLine:edit')")
    @PutMapping
    public Result edit(@RequestBody SysPoLine sysPoLine)
    {
        return toAjax(sysPoLineService.updateSysPoLine(sysPoLine));
    }

    /**
     * 删除采购订单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:poLine:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysPoLineService.deleteSysPoLineByIds(ids));
    }
}
