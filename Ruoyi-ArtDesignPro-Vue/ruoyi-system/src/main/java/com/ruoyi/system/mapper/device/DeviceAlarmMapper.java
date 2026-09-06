package com.ruoyi.system.mapper.device;

import java.util.List;
import com.ruoyi.system.domain.device.DeviceAlarm;

public interface DeviceAlarmMapper
{
    public DeviceAlarm selectDeviceAlarmById(Long alarmId);

    public List<DeviceAlarm> selectDeviceAlarmList(DeviceAlarm alarm);

    public int insertDeviceAlarm(DeviceAlarm alarm);

    public int updateDeviceAlarm(DeviceAlarm alarm);

    public int deleteDeviceAlarmByIds(Long[] alarmIds);
}
