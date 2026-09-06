package com.ruoyi.system.service.device.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.system.domain.device.DeviceChute;
import com.ruoyi.system.mapper.device.DeviceChuteMapper;
import com.ruoyi.system.service.device.IDeviceChuteService;

@Service
public class DeviceChuteServiceImpl implements IDeviceChuteService
{
    @Autowired
    private DeviceChuteMapper chuteMapper;

    @Override
    public DeviceChute selectDeviceChuteById(Long chuteId)
    {
        return chuteMapper.selectDeviceChuteById(chuteId);
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<DeviceChute> selectDeviceChuteList(DeviceChute chute)
    {
        return chuteMapper.selectDeviceChuteList(chute);
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<DeviceChute> selectDeviceChuteOptions(DeviceChute chute)
    {
        return chuteMapper.selectDeviceChuteOptions(chute);
    }

    @Override
    public int insertDeviceChute(DeviceChute chute)
    {
        return chuteMapper.insertDeviceChute(chute);
    }

    @Override
    public int updateDeviceChute(DeviceChute chute)
    {
        return chuteMapper.updateDeviceChute(chute);
    }

    @Override
    public int deleteDeviceChuteByIds(Long[] chuteIds)
    {
        return chuteMapper.deleteDeviceChuteByIds(chuteIds);
    }
}
