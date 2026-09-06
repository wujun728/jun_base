package com.ruoyi.web.controller.device;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
import com.ruoyi.system.domain.device.DeviceAlarm;
import com.ruoyi.system.service.device.IDeviceAlarmService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "报警器管理")
@RestController
@RequestMapping("/device/alarm")
public class DeviceAlarmController extends BaseController
{
    @Autowired
    private IDeviceAlarmService alarmService;

    @PreAuthorize("@ss.hasPermi('device:alarm:list')")
    @Operation(summary = "查询报警器管理列表")
    @GetMapping("/list")
    public TableDataInfo list(DeviceAlarm alarm)
    {
        startPage();
        List<DeviceAlarm> list = alarmService.selectDeviceAlarmList(alarm);
        return getDataTable(list);
    }

    @Log(title = "报警器管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('device:alarm:export')")
    @Operation(summary = "导出报警器管理")
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceAlarm alarm)
    {
        List<DeviceAlarm> list = alarmService.selectDeviceAlarmList(alarm);
        ExcelUtil<DeviceAlarm> util = new ExcelUtil<DeviceAlarm>(DeviceAlarm.class);
        util.exportExcel(response, list, "报警器数据");
    }

    @PreAuthorize("@ss.hasPermi('device:alarm:query')")
    @Operation(summary = "获取报警器管理详情")
    @GetMapping("/{alarmId}")
    @io.swagger.v3.oas.annotations.Parameter(description = "报警器ID")
    public AjaxResult getInfo(@PathVariable Long alarmId)
    {
        return success(alarmService.selectDeviceAlarmById(alarmId));
    }

    @PreAuthorize("@ss.hasPermi('device:alarm:add')")
    @Log(title = "报警器管理", businessType = BusinessType.INSERT)
    @Operation(summary = "新增报警器管理")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody DeviceAlarm alarm)
    {
        alarm.setCreateBy(getUsername());
        if (alarm.getStatus() == null || alarm.getStatus().isEmpty())
        {
            alarm.setStatus("0");
        }
        return toAjax(alarmService.insertDeviceAlarm(alarm));
    }

    @PreAuthorize("@ss.hasPermi('device:alarm:edit')")
    @Log(title = "报警器管理", businessType = BusinessType.UPDATE)
    @Operation(summary = "修改报警器管理")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody DeviceAlarm alarm)
    {
        alarm.setUpdateBy(getUsername());
        return toAjax(alarmService.updateDeviceAlarm(alarm));
    }

    @PreAuthorize("@ss.hasPermi('device:alarm:remove')")
    @Log(title = "报警器管理", businessType = BusinessType.DELETE)
    @Operation(summary = "删除报警器管理")
    @DeleteMapping("/{alarmIds}")
    @io.swagger.v3.oas.annotations.Parameter(description = "报警器ID数组")
    public AjaxResult remove(@PathVariable Long[] alarmIds)
    {
        return toAjax(alarmService.deleteDeviceAlarmByIds(alarmIds));
    }
}
