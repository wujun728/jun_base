package com.ruoyi.system.service.device;

import java.util.List;
import com.ruoyi.system.domain.device.DeviceAlarm;

public interface IDeviceAlarmService
{
    public DeviceAlarm selectDeviceAlarmById(Long alarmId);

    public List<DeviceAlarm> selectDeviceAlarmList(DeviceAlarm alarm);

    public int insertDeviceAlarm(DeviceAlarm alarm);

    public int updateDeviceAlarm(DeviceAlarm alarm);

    public int deleteDeviceAlarmByIds(Long[] alarmIds);
}
