package com.ruoyi.system.service.device;

import java.util.List;
import com.ruoyi.system.domain.device.DeviceChute;

public interface IDeviceChuteService
{
    public DeviceChute selectDeviceChuteById(Long chuteId);

    public List<DeviceChute> selectDeviceChuteList(DeviceChute chute);

    public List<DeviceChute> selectDeviceChuteOptions(DeviceChute chute);

    public int insertDeviceChute(DeviceChute chute);

    public int updateDeviceChute(DeviceChute chute);

    public int deleteDeviceChuteByIds(Long[] chuteIds);
}
