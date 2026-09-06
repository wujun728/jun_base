package com.ruoyi.system.service.device.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.system.domain.device.DeviceAlarm;
import com.ruoyi.system.mapper.device.DeviceAlarmMapper;
import com.ruoyi.system.service.device.IDeviceAlarmService;

@Service
public class DeviceAlarmServiceImpl implements IDeviceAlarmService
{
    @Autowired
    private DeviceAlarmMapper alarmMapper;

    @Override
    public DeviceAlarm selectDeviceAlarmById(Long alarmId)
    {
        return alarmMapper.selectDeviceAlarmById(alarmId);
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<DeviceAlarm> selectDeviceAlarmList(DeviceAlarm alarm)
    {
        return alarmMapper.selectDeviceAlarmList(alarm);
    }

    @Override
    public int insertDeviceAlarm(DeviceAlarm alarm)
    {
        return alarmMapper.insertDeviceAlarm(alarm);
    }

    @Override
    public int updateDeviceAlarm(DeviceAlarm alarm)
    {
        return alarmMapper.updateDeviceAlarm(alarm);
    }

    @Override
    public int deleteDeviceAlarmByIds(Long[] alarmIds)
    {
        return alarmMapper.deleteDeviceAlarmByIds(alarmIds);
    }
}
