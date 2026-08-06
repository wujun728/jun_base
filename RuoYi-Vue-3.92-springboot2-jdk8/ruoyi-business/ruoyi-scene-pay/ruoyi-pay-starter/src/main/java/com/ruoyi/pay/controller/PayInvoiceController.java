package com.ruoyi.pay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.pay.domain.PayInvoice;
import com.ruoyi.pay.service.IPayInvoiceService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletResponse;

/**
 * 发票Controller
 * 
 * @author geek
 * @date 2024-06-11
 */
@RestController
@RequestMapping("/pay/invoice")
@Api(tags = "【发票】管理")
public class PayInvoiceController extends BaseController
{
    @Autowired
    private IPayInvoiceService payInvoiceService;

    /**
     * 查询发票列表
     */
    @ApiOperation("查询发票列表")
    @PreAuthorize("@ss.hasPermi('pay:invoice:list')")
    @GetMapping("/list")
    public TableDataInfo list(PayInvoice payInvoice)
    {
        startPage();
        List<PayInvoice> list = payInvoiceService.selectPayInvoiceList(payInvoice);
        return getDataTable(list);
    }

    /**
     * 导出发票列表
     */
    @ApiOperation("导出发票列表")
    @PreAuthorize("@ss.hasPermi('pay:invoice:export')")
    @Log(title = "发票", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PayInvoice payInvoice)
    {
        List<PayInvoice> list = payInvoiceService.selectPayInvoiceList(payInvoice);
        ExcelUtil<PayInvoice> util = new ExcelUtil<>(PayInvoice.class);
        util.exportExcel(response, list, "发票数据");
    }

    /**
     * 获取发票详细信息
     */
    @ApiOperation("获取发票详细信息")
    @PreAuthorize("@ss.hasPermi('pay:invoice:query')")
    @GetMapping(value = "/{invoiceId}")
    public AjaxResult getInfo(@PathVariable("invoiceId") Long invoiceId)
    {
        return success(payInvoiceService.selectPayInvoiceByInvoiceId(invoiceId));
    }

    /**
     * 新增发票
     */
    @ApiOperation("新增发票")
    @PreAuthorize("@ss.hasPermi('pay:invoice:add')")
    @Log(title = "发票", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PayInvoice payInvoice)
    {
        return toAjax(payInvoiceService.insertPayInvoice(payInvoice));
    }

    /**
     * 修改发票
     */
    @ApiOperation("修改发票")
    @PreAuthorize("@ss.hasPermi('pay:invoice:edit')")
    @Log(title = "发票", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PayInvoice payInvoice)
    {
        return toAjax(payInvoiceService.updatePayInvoice(payInvoice));
    }

    /**
     * 删除发票
     */
    @ApiOperation("删除发票")
    @PreAuthorize("@ss.hasPermi('pay:invoice:remove')")
    @Log(title = "发票", businessType = BusinessType.DELETE)
	@DeleteMapping("/{invoiceIds}")
    public AjaxResult remove(@PathVariable( name = "invoiceIds" ) Long[] invoiceIds) 
    {
        return toAjax(payInvoiceService.deletePayInvoiceByInvoiceIds(invoiceIds));
    }
}
