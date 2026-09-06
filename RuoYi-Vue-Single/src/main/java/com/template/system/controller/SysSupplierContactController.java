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
import com.template.system.entity.SysSupplierContact;
import com.template.system.service.ISysSupplierContactService;
import com.template.framework.utils.poi.ExcelUtil;
import com.template.framework.core.page.TableDataInfo;

/**
 * 供货商联系人Controller
 *
 * @author template
 * @date 2025-10-04
 */
@RestController
@RequestMapping("/system/supplierContact")
public class SysSupplierContactController extends BaseController
{
    @Autowired
    private ISysSupplierContactService sysSupplierContactService;

    /**
     * 查询供货商联系人列表
     */
    @PreAuthorize("@ss.hasPermi('system:supplierContact:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysSupplierContact sysSupplierContact)
    {
        startPage();
        List<SysSupplierContact> list = sysSupplierContactService.selectSysSupplierContactList(sysSupplierContact);
        return getDataTable(list);
    }

    /**
     * 导出供货商联系人列表
     */
    @PreAuthorize("@ss.hasPermi('system:supplierContact:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysSupplierContact sysSupplierContact)
    {
        List<SysSupplierContact> list = sysSupplierContactService.selectSysSupplierContactList(sysSupplierContact);
        ExcelUtil<SysSupplierContact> util = new ExcelUtil<SysSupplierContact>(SysSupplierContact.class);
        util.exportExcel(response, list, "供货商联系人数据");
    }

    /**
     * 获取供货商联系人详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:supplierContact:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(sysSupplierContactService.selectSysSupplierContactById(id));
    }

    /**
     * 新增供货商联系人
     */
    @PreAuthorize("@ss.hasPermi('system:supplierContact:add')")
    @PostMapping
    public Result add(@RequestBody SysSupplierContact sysSupplierContact)
    {
        return toAjax(sysSupplierContactService.insertSysSupplierContact(sysSupplierContact));
    }

    /**
     * 修改供货商联系人
     */
    @PreAuthorize("@ss.hasPermi('system:supplierContact:edit')")
    @PutMapping
    public Result edit(@RequestBody SysSupplierContact sysSupplierContact)
    {
        return toAjax(sysSupplierContactService.updateSysSupplierContact(sysSupplierContact));
    }

    /**
     * 删除供货商联系人
     */
    @PreAuthorize("@ss.hasPermi('system:supplierContact:remove')")
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toAjax(sysSupplierContactService.deleteSysSupplierContactByIds(ids));
    }
}
