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
import com.template.system.entity.SysLoanReturnLine;
import com.template.system.service.ISysLoanReturnLineService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 归还单-明细Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/loanReturnLine")
public class SysLoanReturnLineController extends BaseController
{
    @Autowired
    private ISysLoanReturnLineService sysLoanReturnLineService;

    /**
     * 查询归还单-明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:loanReturnLine:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLoanReturnLine sysLoanReturnLine)
    {
        startPage();
        List<SysLoanReturnLine> list = sysLoanReturnLineService.selectSysLoanReturnLineList(sysLoanReturnLine);
        return getDataTable(list);
    }

    /**
     * 导出归还单-明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:loanReturnLine:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLoanReturnLine sysLoanReturnLine)
    {
        List<SysLoanReturnLine> list = sysLoanReturnLineService.selectSysLoanReturnLineList(sysLoanReturnLine);
        ExcelUtil<SysLoanReturnLine> util = new ExcelUtil<SysLoanReturnLine>(SysLoanReturnLine.class);
        util.exportExcel(response, list, "归还单-明细数据");
    }

    /**
     * 获取归还单-明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:loanReturnLine:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysLoanReturnLineService.selectSysLoanReturnLineById(id));
    }

    /**
     * 新增归还单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:loanReturnLine:add')")
    @PostMapping
    public Result add(@RequestBody SysLoanReturnLine sysLoanReturnLine)
    {
        return toAjax(sysLoanReturnLineService.insertSysLoanReturnLine(sysLoanReturnLine));
    }

    /**
     * 修改归还单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:loanReturnLine:edit')")
    @PutMapping
    public Result edit(@RequestBody SysLoanReturnLine sysLoanReturnLine)
    {
        return toAjax(sysLoanReturnLineService.updateSysLoanReturnLine(sysLoanReturnLine));
    }

    /**
     * 删除归还单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:loanReturnLine:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysLoanReturnLineService.deleteSysLoanReturnLineByIds(ids));
    }
}
