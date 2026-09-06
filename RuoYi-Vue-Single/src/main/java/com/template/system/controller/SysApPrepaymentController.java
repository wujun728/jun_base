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
import com.template.system.entity.SysApPrepayment;
import com.template.system.service.ISysApPrepaymentService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 采购预付款登记Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/apPrepayment")
public class SysApPrepaymentController extends BaseController
{
    @Autowired
    private ISysApPrepaymentService sysApPrepaymentService;

    /**
     * 查询采购预付款登记列表
     */
    @PreAuthorize("@ss.hasPermi('system:apPrepayment:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysApPrepayment sysApPrepayment)
    {
        startPage();
        List<SysApPrepayment> list = sysApPrepaymentService.selectSysApPrepaymentList(sysApPrepayment);
        return getDataTable(list);
    }

    /**
     * 导出采购预付款登记列表
     */
    @PreAuthorize("@ss.hasPermi('system:apPrepayment:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysApPrepayment sysApPrepayment)
    {
        List<SysApPrepayment> list = sysApPrepaymentService.selectSysApPrepaymentList(sysApPrepayment);
        ExcelUtil<SysApPrepayment> util = new ExcelUtil<SysApPrepayment>(SysApPrepayment.class);
        util.exportExcel(response, list, "采购预付款登记数据");
    }

    /**
     * 获取采购预付款登记详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:apPrepayment:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysApPrepaymentService.selectSysApPrepaymentById(id));
    }

    /**
     * 新增采购预付款登记
     */
    @PreAuthorize("@ss.hasPermi('system:apPrepayment:add')")
    @PostMapping
    public Result add(@RequestBody SysApPrepayment sysApPrepayment)
    {
        return toAjax(sysApPrepaymentService.insertSysApPrepayment(sysApPrepayment));
    }

    /**
     * 修改采购预付款登记
     */
    @PreAuthorize("@ss.hasPermi('system:apPrepayment:edit')")
    @PutMapping
    public Result edit(@RequestBody SysApPrepayment sysApPrepayment)
    {
        return toAjax(sysApPrepaymentService.updateSysApPrepayment(sysApPrepayment));
    }

    /**
     * 删除采购预付款登记
     */
    @PreAuthorize("@ss.hasPermi('system:apPrepayment:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysApPrepaymentService.deleteSysApPrepaymentByIds(ids));
    }
}
