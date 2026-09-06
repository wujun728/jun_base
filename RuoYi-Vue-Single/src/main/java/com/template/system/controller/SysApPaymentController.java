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
import com.template.system.entity.SysApPayment;
import com.template.system.service.ISysApPaymentService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 采购付款登记Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/apPayment")
public class SysApPaymentController extends BaseController
{
    @Autowired
    private ISysApPaymentService sysApPaymentService;

    /**
     * 查询采购付款登记列表
     */
    @PreAuthorize("@ss.hasPermi('system:apPayment:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysApPayment sysApPayment)
    {
        startPage();
        List<SysApPayment> list = sysApPaymentService.selectSysApPaymentList(sysApPayment);
        return getDataTable(list);
    }

    /**
     * 导出采购付款登记列表
     */
    @PreAuthorize("@ss.hasPermi('system:apPayment:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysApPayment sysApPayment)
    {
        List<SysApPayment> list = sysApPaymentService.selectSysApPaymentList(sysApPayment);
        ExcelUtil<SysApPayment> util = new ExcelUtil<SysApPayment>(SysApPayment.class);
        util.exportExcel(response, list, "采购付款登记数据");
    }

    /**
     * 获取采购付款登记详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:apPayment:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysApPaymentService.selectSysApPaymentById(id));
    }

    /**
     * 新增采购付款登记
     */
    @PreAuthorize("@ss.hasPermi('system:apPayment:add')")
    @PostMapping
    public Result add(@RequestBody SysApPayment sysApPayment)
    {
        return toAjax(sysApPaymentService.insertSysApPayment(sysApPayment));
    }

    /**
     * 修改采购付款登记
     */
    @PreAuthorize("@ss.hasPermi('system:apPayment:edit')")
    @PutMapping
    public Result edit(@RequestBody SysApPayment sysApPayment)
    {
        return toAjax(sysApPaymentService.updateSysApPayment(sysApPayment));
    }

    /**
     * 删除采购付款登记
     */
    @PreAuthorize("@ss.hasPermi('system:apPayment:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysApPaymentService.deleteSysApPaymentByIds(ids));
    }
}
