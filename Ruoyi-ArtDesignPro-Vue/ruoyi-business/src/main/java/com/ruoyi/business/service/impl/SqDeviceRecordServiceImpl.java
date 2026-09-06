package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqDeviceRecord;
import com.ruoyi.business.mapper.SqDeviceRecordMapper;
import com.ruoyi.business.service.ISqDeviceRecordService;

@Service
public class SqDeviceRecordServiceImpl implements ISqDeviceRecordService
{
    @Autowired
    private SqDeviceRecordMapper sqDeviceRecordMapper;

    @Override
    public SqDeviceRecord selectSqDeviceRecordById(Long id)
    {
        return sqDeviceRecordMapper.selectSqDeviceRecordById(id);
    }

    @Override
    public List<SqDeviceRecord> selectSqDeviceRecordList(SqDeviceRecord record)
    {
        return sqDeviceRecordMapper.selectSqDeviceRecordList(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSqDeviceRecord(SqDeviceRecord record)
    {
        return sqDeviceRecordMapper.insertSqDeviceRecord(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSqDeviceRecord(SqDeviceRecord record)
    {
        return sqDeviceRecordMapper.updateSqDeviceRecord(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqDeviceRecordByIds(Long[] ids)
    {
        return sqDeviceRecordMapper.deleteSqDeviceRecordByIds(ids);
    }
}
