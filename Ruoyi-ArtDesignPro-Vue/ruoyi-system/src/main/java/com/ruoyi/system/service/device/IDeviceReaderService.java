package com.ruoyi.system.service.device;

import java.util.List;
import com.ruoyi.system.domain.device.DeviceReader;

public interface IDeviceReaderService
{
    public DeviceReader selectDeviceReaderById(Long readerId);

    public List<DeviceReader> selectDeviceReaderList(DeviceReader reader);

    public int insertDeviceReader(DeviceReader reader);

    public int updateDeviceReader(DeviceReader reader);

    public int deleteDeviceReaderByIds(Long[] readerIds);
}
