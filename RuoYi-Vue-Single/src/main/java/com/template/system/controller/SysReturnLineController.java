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
import com.template.system.entity.SysReturnLine;
import com.template.system.service.ISysReturnLineService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 退库单-明细Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/returnLine")
public class SysReturnLineController extends BaseController
{
    @Autowired
    private ISysReturnLineService sysReturnLineService;

    /**
     * 查询退库单-明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:returnLine:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysReturnLine sysReturnLine)
    {
        startPage();
        List<SysReturnLine> list = sysReturnLineService.selectSysReturnLineList(sysReturnLine);
        return getDataTable(list);
    }

    /**
     * 导出退库单-明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:returnLine:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysReturnLine sysReturnLine)
    {
        List<SysReturnLine> list = sysReturnLineService.selectSysReturnLineList(sysReturnLine);
        ExcelUtil<SysReturnLine> util = new ExcelUtil<SysReturnLine>(SysReturnLine.class);
        util.exportExcel(response, list, "退库单-明细数据");
    }

    /**
     * 获取退库单-明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:returnLine:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysReturnLineService.selectSysReturnLineById(id));
    }

    /**
     * 新增退库单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:returnLine:add')")
    @PostMapping
    public Result add(@RequestBody SysReturnLine sysReturnLine)
    {
        return toAjax(sysReturnLineService.insertSysReturnLine(sysReturnLine));
    }

    /**
     * 修改退库单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:returnLine:edit')")
    @PutMapping
    public Result edit(@RequestBody SysReturnLine sysReturnLine)
    {
        return toAjax(sysReturnLineService.updateSysReturnLine(sysReturnLine));
    }

    /**
     * 删除退库单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:returnLine:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysReturnLineService.deleteSysReturnLineByIds(ids));
    }
}
