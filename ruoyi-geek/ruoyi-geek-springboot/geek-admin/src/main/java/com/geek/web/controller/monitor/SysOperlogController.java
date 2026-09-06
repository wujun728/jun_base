package com.geek.web.controller.monitor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geek.common.annotation.Log;
import com.geek.common.core.controller.BaseController;
import com.geek.common.core.domain.AjaxResult;
import com.geek.common.core.domain.R;
import com.geek.common.core.page.PageDomain;
import com.geek.common.core.page.TableDataInfo;
import com.geek.common.core.page.TableSupport;
import com.geek.common.enums.BusinessType;
import com.geek.system.domain.SysOperLog;
import com.geek.system.service.ISysOperLogService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 操作日志记录
 * 
 * @author geek
 */
@Tag(name = "操作日志记录")
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController {

    @Autowired
    private ISysOperLogService operLogService;

    @Operation(summary = "获取操作日志记录列表")
    @PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
    @GetMapping("/list")
    public TableDataInfo<SysOperLog> list(SysOperLog operLog) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<SysOperLog> list = operLogService.page(operLog, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    @Operation(summary = "导出操作日志记录列表")
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOperLog operLog) {
        operLogService.export(operLog, response);
    }

    @Operation(summary = "删除操作日志记录")
    @Parameters({
            @Parameter(name = "operIds", description = "记录id数组", required = true),
    })
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/{operIds}")
    public AjaxResult remove(@PathVariable(name = "operIds") List<Long> operIds) {
        return toAjax(operLogService.removeByIds(operIds));
    }

    @Operation(summary = "清除操作日志记录")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        operLogService.cleanOperLog();
        return success();
    }

    @Operation(summary = "业务监控")
    @GetMapping("/business")
    public R<Map<String, Object>> business(SysOperLog operLog) {
        // 查询并获取统计数据
        List<Map<String, Object>> successStats = operLogService.getSuccessOperationStats(operLog);
        List<Map<String, Object>> failureStats = operLogService.getFailureOperationStats(operLog);
        List<Map<String, Object>> statusStats = operLogService.getStatusStats(operLog);
        List<Map<String, Object>> moduleOperationStats = operLogService.getModuleOperationStats(operLog);
        // 创建一个新的 Map 来组织数据
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("successStats", successStats);
        result.put("failureStats", failureStats);
        result.put("statusStats", statusStats);
        result.put("moduleOperationStats", moduleOperationStats);
        result.put("total",
                successStats.size() + failureStats.size() + statusStats.size() + moduleOperationStats.size());
        return R.ok(result);
    }
}
