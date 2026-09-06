package com.ruoyi.web.controller.device;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.device.DeviceChute;
import com.ruoyi.system.service.device.IDeviceChuteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "溜井管理")
@RestController
@RequestMapping("/device/chute")
public class DeviceChuteController extends BaseController
{
    @Autowired
    private IDeviceChuteService chuteService;

    @PreAuthorize("@ss.hasAnyPermi('device:camera:list,device:alarm:list,device:reader:list')")
    @Operation(summary = "查询溜井下拉选项")
    @GetMapping("/options")
    public AjaxResult options()
    {
        return success(chuteService.selectDeviceChuteOptions(new DeviceChute()));
    }

    @PreAuthorize("@ss.hasAnyPermi('device:camera:add,device:alarm:add,device:reader:add')")
    @Log(title = "溜井信息", businessType = BusinessType.INSERT)
    @Operation(summary = "新增溜井管理")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody DeviceChute chute)
    {
        chute.setCreateBy(getUsername());
        if (chute.getStatus() == null || chute.getStatus().isEmpty())
        {
            chute.setStatus("0");
        }
        int rows = chuteService.insertDeviceChute(chute);
        AjaxResult result = toAjax(rows);
        if (rows > 0)
        {
            result.put("data", chute);
        }
        return result;
    }
}
