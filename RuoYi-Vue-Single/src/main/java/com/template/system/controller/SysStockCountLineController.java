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
import com.template.system.entity.SysStockCountLine;
import com.template.system.service.ISysStockCountLineService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 盘点单-明细Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/stockCountLine")
public class SysStockCountLineController extends BaseController
{
    @Autowired
    private ISysStockCountLineService sysStockCountLineService;

    /**
     * 查询盘点单-明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:stockCountLine:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysStockCountLine sysStockCountLine)
    {
        startPage();
        List<SysStockCountLine> list = sysStockCountLineService.selectSysStockCountLineList(sysStockCountLine);
        return getDataTable(list);
    }

    /**
     * 导出盘点单-明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:stockCountLine:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysStockCountLine sysStockCountLine)
    {
        List<SysStockCountLine> list = sysStockCountLineService.selectSysStockCountLineList(sysStockCountLine);
        ExcelUtil<SysStockCountLine> util = new ExcelUtil<SysStockCountLine>(SysStockCountLine.class);
        util.exportExcel(response, list, "盘点单-明细数据");
    }

    /**
     * 获取盘点单-明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:stockCountLine:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysStockCountLineService.selectSysStockCountLineById(id));
    }

    /**
     * 新增盘点单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:stockCountLine:add')")
    @PostMapping
    public Result add(@RequestBody SysStockCountLine sysStockCountLine)
    {
        return toAjax(sysStockCountLineService.insertSysStockCountLine(sysStockCountLine));
    }

    /**
     * 修改盘点单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:stockCountLine:edit')")
    @PutMapping
    public Result edit(@RequestBody SysStockCountLine sysStockCountLine)
    {
        return toAjax(sysStockCountLineService.updateSysStockCountLine(sysStockCountLine));
    }

    /**
     * 删除盘点单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:stockCountLine:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysStockCountLineService.deleteSysStockCountLineByIds(ids));
    }
}
