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
import com.ruoyi.system.domain.device.DeviceCamera;
import com.ruoyi.system.service.device.IDeviceCameraService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "摄像头管理")
@RestController
@RequestMapping("/device/camera")
public class DeviceCameraController extends BaseController
{
    @Autowired
    private IDeviceCameraService cameraService;

    @PreAuthorize("@ss.hasPermi('device:camera:list')")
    @Operation(summary = "查询摄像头管理列表")
    @GetMapping("/list")
    public TableDataInfo list(DeviceCamera camera)
    {
        startPage();
        List<DeviceCamera> list = cameraService.selectDeviceCameraList(camera);
        return getDataTable(list);
    }

    @Log(title = "摄像头管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('device:camera:export')")
    @Operation(summary = "导出摄像头管理")
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceCamera camera)
    {
        List<DeviceCamera> list = cameraService.selectDeviceCameraList(camera);
        ExcelUtil<DeviceCamera> util = new ExcelUtil<DeviceCamera>(DeviceCamera.class);
        util.exportExcel(response, list, "摄像头数据");
    }

    @PreAuthorize("@ss.hasPermi('device:camera:query')")
    @Operation(summary = "获取摄像头管理详情")
    @GetMapping("/{cameraId}")
    @io.swagger.v3.oas.annotations.Parameter(description = "摄像头ID")
    public AjaxResult getInfo(@PathVariable Long cameraId)
    {
        return success(cameraService.selectDeviceCameraById(cameraId));
    }

    @PreAuthorize("@ss.hasPermi('device:camera:add')")
    @Log(title = "摄像头管理", businessType = BusinessType.INSERT)
    @Operation(summary = "新增摄像头管理")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody DeviceCamera camera)
    {
        camera.setCreateBy(getUsername());
        if (camera.getStatus() == null || camera.getStatus().isEmpty())
        {
            camera.setStatus("0");
        }
        return toAjax(cameraService.insertDeviceCamera(camera));
    }

    @PreAuthorize("@ss.hasPermi('device:camera:edit')")
    @Log(title = "摄像头管理", businessType = BusinessType.UPDATE)
    @Operation(summary = "修改摄像头管理")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody DeviceCamera camera)
    {
        camera.setUpdateBy(getUsername());
        return toAjax(cameraService.updateDeviceCamera(camera));
    }

    @PreAuthorize("@ss.hasPermi('device:camera:remove')")
    @Log(title = "摄像头管理", businessType = BusinessType.DELETE)
    @Operation(summary = "删除摄像头管理")
    @DeleteMapping("/{cameraIds}")
    @io.swagger.v3.oas.annotations.Parameter(description = "摄像头ID数组")
    public AjaxResult remove(@PathVariable Long[] cameraIds)
    {
        return toAjax(cameraService.deleteDeviceCameraByIds(cameraIds));
    }
}
