package com.ruoyi.web.controller.system;

import java.util.List;

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
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysServer;
import com.ruoyi.system.service.ISysServerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 服务注册Controller
 *
 * @author ruoyi
 * @date 2021-08-20
 */
@RestController
@RequestMapping("/system/server")
public class SysServerController extends BaseController {
    @Autowired
    private ISysServerService sysServerService;

    /**
     * 查询服务注册列表
     */
    @PreAuthorize("@ss.hasPermi('system:server:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysServer sysServer) {
        startPage();
        List<SysServer> list = sysServerService.selectSysServerList(sysServer);
        return getDataTable(list);
    }

    /**
     * 导出服务注册列表
     */
    @PreAuthorize("@ss.hasPermi('system:server:export')")
    @Log(title = "服务注册", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysServer sysServer) {
        List<SysServer> list = sysServerService.selectSysServerList(sysServer);
        ExcelUtil<SysServer> util = new ExcelUtil<SysServer>(SysServer.class);
        return util.exportExcel(list, "服务注册数据");
    }

    /**
     * 获取服务注册详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:server:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysServerService.selectSysServerById(id));
    }

    /**
     * 新增服务注册
     */
    @PreAuthorize("@ss.hasPermi('system:server:add')")
    @Log(title = "服务注册", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysServer sysServer) {
        return toAjax(sysServerService.insertSysServer(sysServer));
    }

    /**
     * 修改服务注册
     */
    @PreAuthorize("@ss.hasPermi('system:server:edit')")
    @Log(title = "服务注册", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysServer sysServer) {
        return toAjax(sysServerService.updateSysServer(sysServer));
    }

    /**
     * 删除服务注册
     */
    @PreAuthorize("@ss.hasPermi('system:server:remove')")
    @Log(title = "服务注册", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysServerService.deleteSysServerByIds(ids));
    }
}
