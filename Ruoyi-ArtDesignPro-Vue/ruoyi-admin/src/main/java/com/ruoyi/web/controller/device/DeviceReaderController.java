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
import com.ruoyi.system.domain.device.DeviceReader;
import com.ruoyi.system.service.device.IDeviceReaderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "阅读器管理")
@RestController
@RequestMapping("/device/reader")
public class DeviceReaderController extends BaseController
{
    @Autowired
    private IDeviceReaderService readerService;

    @PreAuthorize("@ss.hasPermi('device:reader:list')")
    @Operation(summary = "查询阅读器管理列表")
    @GetMapping("/list")
    public TableDataInfo list(DeviceReader reader)
    {
        startPage();
        List<DeviceReader> list = readerService.selectDeviceReaderList(reader);
        return getDataTable(list);
    }

    @Log(title = "阅读器管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('device:reader:export')")
    @Operation(summary = "导出阅读器管理")
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceReader reader)
    {
        List<DeviceReader> list = readerService.selectDeviceReaderList(reader);
        ExcelUtil<DeviceReader> util = new ExcelUtil<DeviceReader>(DeviceReader.class);
        util.exportExcel(response, list, "阅读器数据");
    }

    @PreAuthorize("@ss.hasPermi('device:reader:query')")
    @Operation(summary = "获取阅读器管理详情")
    @GetMapping("/{readerId}")
    @io.swagger.v3.oas.annotations.Parameter(description = "阅读器ID")
    public AjaxResult getInfo(@PathVariable Long readerId)
    {
        return success(readerService.selectDeviceReaderById(readerId));
    }

    @PreAuthorize("@ss.hasPermi('device:reader:add')")
    @Log(title = "阅读器管理", businessType = BusinessType.INSERT)
    @Operation(summary = "新增阅读器管理")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody DeviceReader reader)
    {
        reader.setCreateBy(getUsername());
        if (reader.getStatus() == null || reader.getStatus().isEmpty())
        {
            reader.setStatus("0");
        }
        return toAjax(readerService.insertDeviceReader(reader));
    }

    @PreAuthorize("@ss.hasPermi('device:reader:edit')")
    @Log(title = "阅读器管理", businessType = BusinessType.UPDATE)
    @Operation(summary = "修改阅读器管理")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody DeviceReader reader)
    {
        reader.setUpdateBy(getUsername());
        return toAjax(readerService.updateDeviceReader(reader));
    }

    @PreAuthorize("@ss.hasPermi('device:reader:remove')")
    @Log(title = "阅读器管理", businessType = BusinessType.DELETE)
    @Operation(summary = "删除阅读器管理")
    @DeleteMapping("/{readerIds}")
    @io.swagger.v3.oas.annotations.Parameter(description = "阅读器ID数组")
    public AjaxResult remove(@PathVariable Long[] readerIds)
    {
        return toAjax(readerService.deleteDeviceReaderByIds(readerIds));
    }
}
