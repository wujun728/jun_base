package com.ruoyi.business.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
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
import com.ruoyi.business.domain.SqOfflineTask;
import com.ruoyi.business.service.ISqOfflineTaskService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/business/sq/offline-task")
public class SqOfflineTaskController extends BaseController
{
    @Autowired
    private ISqOfflineTaskService sqOfflineTaskService;

    @PreAuthorize("@ss.hasPermi('business:offlineTask:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqOfflineTask task)
    {
        startPage();
        return getDataTable(sqOfflineTaskService.selectSqOfflineTaskList(task));
    }

    @Log(title = "溜井离线任务", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:offlineTask:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqOfflineTask task)
    {
        List<SqOfflineTask> list = sqOfflineTaskService.selectSqOfflineTaskList(task);
        new ExcelUtil<SqOfflineTask>(SqOfflineTask.class).exportExcel(response, list, "溜井离线任务");
    }

    @PreAuthorize("@ss.hasPermi('business:offlineTask:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(sqOfflineTaskService.selectSqOfflineTaskById(id));
    }

    @Log(title = "溜井离线任务", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:offlineTask:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SqOfflineTask task)
    {
        return toAjax(sqOfflineTaskService.insertSqOfflineTask(task));
    }

    @Log(title = "溜井离线任务", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:offlineTask:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody SqOfflineTask task)
    {
        return toAjax(sqOfflineTaskService.updateSqOfflineTask(task));
    }

    @Log(title = "溜井离线任务状态", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:offlineTask:edit')")
    @PutMapping("/status")
    public AjaxResult editStatus(@RequestBody SqOfflineTask task)
    {
        return toAjax(sqOfflineTaskService.updateSqOfflineTaskStatus(task));
    }

    @Log(title = "溜井离线任务", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:offlineTask:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sqOfflineTaskService.deleteSqOfflineTaskByIds(ids));
    }
}
