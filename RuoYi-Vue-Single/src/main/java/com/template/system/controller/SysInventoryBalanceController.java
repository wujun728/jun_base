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
import com.template.system.entity.SysInventoryBalance;
import com.template.system.service.ISysInventoryBalanceService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 库存现势(按仓库+产品)Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/inventoryBalance")
public class SysInventoryBalanceController extends BaseController
{
    @Autowired
    private ISysInventoryBalanceService sysInventoryBalanceService;

    /**
     * 查询库存现势(按仓库+产品)列表
     */
    @PreAuthorize("@ss.hasPermi('system:inventoryBalance:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysInventoryBalance sysInventoryBalance)
    {
        startPage();
        List<SysInventoryBalance> list = sysInventoryBalanceService.selectSysInventoryBalanceList(sysInventoryBalance);
        return getDataTable(list);
    }

    /**
     * 导出库存现势(按仓库+产品)列表
     */
    @PreAuthorize("@ss.hasPermi('system:inventoryBalance:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysInventoryBalance sysInventoryBalance)
    {
        List<SysInventoryBalance> list = sysInventoryBalanceService.selectSysInventoryBalanceList(sysInventoryBalance);
        ExcelUtil<SysInventoryBalance> util = new ExcelUtil<SysInventoryBalance>(SysInventoryBalance.class);
        util.exportExcel(response, list, "库存现势(按仓库+产品)数据");
    }

    /**
     * 获取库存现势(按仓库+产品)详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:inventoryBalance:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysInventoryBalanceService.selectSysInventoryBalanceById(id));
    }

    /**
     * 新增库存现势(按仓库+产品)
     */
    @PreAuthorize("@ss.hasPermi('system:inventoryBalance:add')")
    @PostMapping
    public Result add(@RequestBody SysInventoryBalance sysInventoryBalance)
    {
        return toAjax(sysInventoryBalanceService.insertSysInventoryBalance(sysInventoryBalance));
    }

    /**
     * 修改库存现势(按仓库+产品)
     */
    @PreAuthorize("@ss.hasPermi('system:inventoryBalance:edit')")
    @PutMapping
    public Result edit(@RequestBody SysInventoryBalance sysInventoryBalance)
    {
        return toAjax(sysInventoryBalanceService.updateSysInventoryBalance(sysInventoryBalance));
    }

    /**
     * 删除库存现势(按仓库+产品)
     */
    @PreAuthorize("@ss.hasPermi('system:inventoryBalance:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysInventoryBalanceService.deleteSysInventoryBalanceByIds(ids));
    }
}
