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
import com.template.system.entity.SysLoanReturnHeader;
import com.template.system.service.ISysLoanReturnHeaderService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 归还单-主Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/loanReturnHeader")
public class SysLoanReturnHeaderController extends BaseController
{
    @Autowired
    private ISysLoanReturnHeaderService sysLoanReturnHeaderService;

    /**
     * 查询归还单-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:loanReturnHeader:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLoanReturnHeader sysLoanReturnHeader)
    {
        startPage();
        List<SysLoanReturnHeader> list = sysLoanReturnHeaderService.selectSysLoanReturnHeaderList(sysLoanReturnHeader);
        return getDataTable(list);
    }

    /**
     * 导出归还单-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:loanReturnHeader:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLoanReturnHeader sysLoanReturnHeader)
    {
        List<SysLoanReturnHeader> list = sysLoanReturnHeaderService.selectSysLoanReturnHeaderList(sysLoanReturnHeader);
        ExcelUtil<SysLoanReturnHeader> util = new ExcelUtil<SysLoanReturnHeader>(SysLoanReturnHeader.class);
        util.exportExcel(response, list, "归还单-主数据");
    }

    /**
     * 获取归还单-主详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:loanReturnHeader:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysLoanReturnHeaderService.selectSysLoanReturnHeaderById(id));
    }

    /**
     * 新增归还单-主
     */
    @PreAuthorize("@ss.hasPermi('system:loanReturnHeader:add')")
    @PostMapping
    public Result add(@RequestBody SysLoanReturnHeader sysLoanReturnHeader)
    {
        return toAjax(sysLoanReturnHeaderService.insertSysLoanReturnHeader(sysLoanReturnHeader));
    }

    /**
     * 修改归还单-主
     */
    @PreAuthorize("@ss.hasPermi('system:loanReturnHeader:edit')")
    @PutMapping
    public Result edit(@RequestBody SysLoanReturnHeader sysLoanReturnHeader)
    {
        return toAjax(sysLoanReturnHeaderService.updateSysLoanReturnHeader(sysLoanReturnHeader));
    }

    /**
     * 删除归还单-主
     */
    @PreAuthorize("@ss.hasPermi('system:loanReturnHeader:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysLoanReturnHeaderService.deleteSysLoanReturnHeaderByIds(ids));
    }
}
