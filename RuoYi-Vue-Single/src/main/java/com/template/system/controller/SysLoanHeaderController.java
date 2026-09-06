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
import com.template.system.entity.SysLoanHeader;
import com.template.system.service.ISysLoanHeaderService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 借出单-主Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/loanHeader")
public class SysLoanHeaderController extends BaseController
{
    @Autowired
    private ISysLoanHeaderService sysLoanHeaderService;

    /**
     * 查询借出单-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:loanHeader:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLoanHeader sysLoanHeader)
    {
        startPage();
        List<SysLoanHeader> list = sysLoanHeaderService.selectSysLoanHeaderList(sysLoanHeader);
        return getDataTable(list);
    }

    /**
     * 导出借出单-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:loanHeader:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLoanHeader sysLoanHeader)
    {
        List<SysLoanHeader> list = sysLoanHeaderService.selectSysLoanHeaderList(sysLoanHeader);
        ExcelUtil<SysLoanHeader> util = new ExcelUtil<SysLoanHeader>(SysLoanHeader.class);
        util.exportExcel(response, list, "借出单-主数据");
    }

    /**
     * 获取借出单-主详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:loanHeader:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysLoanHeaderService.selectSysLoanHeaderById(id));
    }

    /**
     * 新增借出单-主
     */
    @PreAuthorize("@ss.hasPermi('system:loanHeader:add')")
    @PostMapping
    public Result add(@RequestBody SysLoanHeader sysLoanHeader)
    {
        return toAjax(sysLoanHeaderService.insertSysLoanHeader(sysLoanHeader));
    }

    /**
     * 修改借出单-主
     */
    @PreAuthorize("@ss.hasPermi('system:loanHeader:edit')")
    @PutMapping
    public Result edit(@RequestBody SysLoanHeader sysLoanHeader)
    {
        return toAjax(sysLoanHeaderService.updateSysLoanHeader(sysLoanHeader));
    }

    /**
     * 删除借出单-主
     */
    @PreAuthorize("@ss.hasPermi('system:loanHeader:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysLoanHeaderService.deleteSysLoanHeaderByIds(ids));
    }
}
