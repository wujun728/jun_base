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
import com.template.system.entity.SysWarehouse;
import com.template.system.service.ISysWarehouseService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 仓库Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/warehouse")
public class SysWarehouseController extends BaseController
{
    @Autowired
    private ISysWarehouseService sysWarehouseService;

    /**
     * 查询仓库列表
     */
    @PreAuthorize("@ss.hasPermi('system:warehouse:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysWarehouse sysWarehouse)
    {
        startPage();
        List<SysWarehouse> list = sysWarehouseService.selectSysWarehouseList(sysWarehouse);
        return getDataTable(list);
    }

    /**
     * 导出仓库列表
     */
    @PreAuthorize("@ss.hasPermi('system:warehouse:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysWarehouse sysWarehouse)
    {
        List<SysWarehouse> list = sysWarehouseService.selectSysWarehouseList(sysWarehouse);
        ExcelUtil<SysWarehouse> util = new ExcelUtil<SysWarehouse>(SysWarehouse.class);
        util.exportExcel(response, list, "仓库数据");
    }

    /**
     * 获取仓库详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:warehouse:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysWarehouseService.selectSysWarehouseById(id));
    }

    /**
     * 新增仓库
     */
    @PreAuthorize("@ss.hasPermi('system:warehouse:add')")
    @PostMapping
    public Result add(@RequestBody SysWarehouse sysWarehouse)
    {
        return toAjax(sysWarehouseService.insertSysWarehouse(sysWarehouse));
    }

    /**
     * 修改仓库
     */
    @PreAuthorize("@ss.hasPermi('system:warehouse:edit')")
    @PutMapping
    public Result edit(@RequestBody SysWarehouse sysWarehouse)
    {
        return toAjax(sysWarehouseService.updateSysWarehouse(sysWarehouse));
    }

    /**
     * 删除仓库
     */
    @PreAuthorize("@ss.hasPermi('system:warehouse:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysWarehouseService.deleteSysWarehouseByIds(ids));
    }
}
