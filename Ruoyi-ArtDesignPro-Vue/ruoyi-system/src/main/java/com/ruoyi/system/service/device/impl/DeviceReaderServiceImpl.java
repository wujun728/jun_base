package com.ruoyi.system.service.device.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.system.domain.device.DeviceReader;
import com.ruoyi.system.mapper.device.DeviceReaderMapper;
import com.ruoyi.system.service.device.IDeviceReaderService;

@Service
public class DeviceReaderServiceImpl implements IDeviceReaderService
{
    @Autowired
    private DeviceReaderMapper readerMapper;

    @Override
    public DeviceReader selectDeviceReaderById(Long readerId)
    {
        return readerMapper.selectDeviceReaderById(readerId);
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<DeviceReader> selectDeviceReaderList(DeviceReader reader)
    {
        return readerMapper.selectDeviceReaderList(reader);
    }

    @Override
    public int insertDeviceReader(DeviceReader reader)
    {
        return readerMapper.insertDeviceReader(reader);
    }

    @Override
    public int updateDeviceReader(DeviceReader reader)
    {
        return readerMapper.updateDeviceReader(reader);
    }

    @Override
    public int deleteDeviceReaderByIds(Long[] readerIds)
    {
        return readerMapper.deleteDeviceReaderByIds(readerIds);
    }
}
