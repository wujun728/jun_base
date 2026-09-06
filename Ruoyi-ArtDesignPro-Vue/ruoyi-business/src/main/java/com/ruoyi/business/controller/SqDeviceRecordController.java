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
import com.ruoyi.business.domain.SqDeviceRecord;
import com.ruoyi.business.service.ISqDeviceRecordService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/business/sq/device-record")
public class SqDeviceRecordController extends BaseController
{
    @Autowired
    private ISqDeviceRecordService sqDeviceRecordService;

    @PreAuthorize("@ss.hasPermi('business:deviceRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqDeviceRecord record)
    {
        startPage();
        return getDataTable(sqDeviceRecordService.selectSqDeviceRecordList(record));
    }

    @Log(title = "设备运行记录", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:deviceRecord:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqDeviceRecord record)
    {
        List<SqDeviceRecord> list = sqDeviceRecordService.selectSqDeviceRecordList(record);
        new ExcelUtil<SqDeviceRecord>(SqDeviceRecord.class).exportExcel(response, list, "设备运行记录");
    }

    @PreAuthorize("@ss.hasPermi('business:deviceRecord:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(sqDeviceRecordService.selectSqDeviceRecordById(id));
    }

    @Log(title = "设备运行记录", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:deviceRecord:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SqDeviceRecord record)
    {
        return toAjax(sqDeviceRecordService.insertSqDeviceRecord(record));
    }

    @Log(title = "设备运行记录", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:deviceRecord:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody SqDeviceRecord record)
    {
        return toAjax(sqDeviceRecordService.updateSqDeviceRecord(record));
    }

    @Log(title = "设备运行记录", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:deviceRecord:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sqDeviceRecordService.deleteSqDeviceRecordByIds(ids));
    }
}
