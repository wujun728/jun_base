package com.ruoyi.system.mapper.device;

import java.util.List;
import com.ruoyi.system.domain.device.DeviceReader;

public interface DeviceReaderMapper
{
    public DeviceReader selectDeviceReaderById(Long readerId);

    public List<DeviceReader> selectDeviceReaderList(DeviceReader reader);

    public int insertDeviceReader(DeviceReader reader);

    public int updateDeviceReader(DeviceReader reader);

    public int deleteDeviceReaderByIds(Long[] readerIds);
}
