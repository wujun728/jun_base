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
import com.template.system.entity.SysLoanLine;
import com.template.system.service.ISysLoanLineService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 借出单-明细Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/loanLine")
public class SysLoanLineController extends BaseController
{
    @Autowired
    private ISysLoanLineService sysLoanLineService;

    /**
     * 查询借出单-明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:loanLine:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLoanLine sysLoanLine)
    {
        startPage();
        List<SysLoanLine> list = sysLoanLineService.selectSysLoanLineList(sysLoanLine);
        return getDataTable(list);
    }

    /**
     * 导出借出单-明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:loanLine:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLoanLine sysLoanLine)
    {
        List<SysLoanLine> list = sysLoanLineService.selectSysLoanLineList(sysLoanLine);
        ExcelUtil<SysLoanLine> util = new ExcelUtil<SysLoanLine>(SysLoanLine.class);
        util.exportExcel(response, list, "借出单-明细数据");
    }

    /**
     * 获取借出单-明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:loanLine:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysLoanLineService.selectSysLoanLineById(id));
    }

    /**
     * 新增借出单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:loanLine:add')")
    @PostMapping
    public Result add(@RequestBody SysLoanLine sysLoanLine)
    {
        return toAjax(sysLoanLineService.insertSysLoanLine(sysLoanLine));
    }

    /**
     * 修改借出单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:loanLine:edit')")
    @PutMapping
    public Result edit(@RequestBody SysLoanLine sysLoanLine)
    {
        return toAjax(sysLoanLineService.updateSysLoanLine(sysLoanLine));
    }

    /**
     * 删除借出单-明细
     */
    @PreAuthorize("@ss.hasPermi('system:loanLine:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysLoanLineService.deleteSysLoanLineByIds(ids));
    }
}
