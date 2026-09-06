package com.ruoyi.system.service.device;

import java.util.List;
import com.ruoyi.system.domain.device.DeviceCamera;

public interface IDeviceCameraService
{
    public DeviceCamera selectDeviceCameraById(Long cameraId);

    public List<DeviceCamera> selectDeviceCameraList(DeviceCamera camera);

    public int insertDeviceCamera(DeviceCamera camera);

    public int updateDeviceCamera(DeviceCamera camera);

    public int deleteDeviceCameraByIds(Long[] cameraIds);
}
