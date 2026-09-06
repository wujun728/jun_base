package com.danny.web.controller.monitor;

import com.danny.common.annotation.Log;
import com.danny.common.core.controller.BaseController;
import com.danny.common.core.domain.R;
import com.danny.common.core.page.PageDomain;
import com.danny.common.enums.BusinessType;
import com.danny.common.utils.poi.ExcelUtil;
import com.danny.system.domain.SysLoginLog;
import com.danny.system.service.SysLoginLogService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 系统访问记录
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/loginLog")
public class SysLoginLogController extends BaseController
{
    @Autowired
    private SysLoginLogService sysLoginLogService;

    @PreAuthorize("@ss.hasPermi('monitor:loginLog:list')")
    @GetMapping("/page")
    public R<PageInfo> list(SysLoginLog sysLoginLog, HttpServletRequest request, HttpServletResponse response)
    {
        sysLoginLog.setPage(new PageDomain(request, response));
        return R.data(sysLoginLogService.findPage(sysLoginLog));
    }

    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:loginLog:export')")
    @GetMapping("/export")
    public R export(SysLoginLog sysLoginLog)
    {
        List<SysLoginLog> list = sysLoginLogService.findList(sysLoginLog);
        ExcelUtil<SysLoginLog> util = new ExcelUtil<SysLoginLog>(SysLoginLog.class);
        return util.exportExcel(list, "登录日志");
    }

    @PreAuthorize("@ss.hasPermi('monitor:loginLog:remove')")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public R remove(@PathVariable String[] infoIds)
    {
        return R.status(sysLoginLogService.deleteLoginLogByIds(infoIds));
    }

    @PreAuthorize("@ss.hasPermi('monitor:loginLog:remove')")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public R clean()
    {
        sysLoginLogService.cleanLoginLog();
        return R.status(true);
    }
}
