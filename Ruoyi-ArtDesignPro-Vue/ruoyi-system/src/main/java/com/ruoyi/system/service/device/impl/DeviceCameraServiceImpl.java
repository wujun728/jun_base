package com.ruoyi.system.service.device.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.device.DeviceCamera;
import com.ruoyi.system.mapper.device.DeviceCameraMapper;
import com.ruoyi.system.service.device.IDeviceCameraService;

@Service
public class DeviceCameraServiceImpl implements IDeviceCameraService
{
    @Autowired
    private DeviceCameraMapper cameraMapper;

    @Override
    public DeviceCamera selectDeviceCameraById(Long cameraId)
    {
        DeviceCamera camera = cameraMapper.selectDeviceCameraById(cameraId);
        maskPassword(camera);
        return camera;
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<DeviceCamera> selectDeviceCameraList(DeviceCamera camera)
    {
        List<DeviceCamera> list = cameraMapper.selectDeviceCameraList(camera);
        list.forEach(this::maskPassword);
        return list;
    }

    @Override
    public int insertDeviceCamera(DeviceCamera camera)
    {
        return cameraMapper.insertDeviceCamera(camera);
    }

    @Override
    public int updateDeviceCamera(DeviceCamera camera)
    {
        if (DeviceCamera.MASKED_PASSWORD.equals(camera.getPassword()))
        {
            camera.setPassword(null);
        }
        return cameraMapper.updateDeviceCamera(camera);
    }

    @Override
    public int deleteDeviceCameraByIds(Long[] cameraIds)
    {
        return cameraMapper.deleteDeviceCameraByIds(cameraIds);
    }

    private void maskPassword(DeviceCamera camera)
    {
        if (camera != null && StringUtils.isNotEmpty(camera.getPassword()))
        {
            camera.setPassword(DeviceCamera.MASKED_PASSWORD);
        }
    }
}
