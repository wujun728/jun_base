package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SqDeviceRecord;

public interface ISqDeviceRecordService
{
    public SqDeviceRecord selectSqDeviceRecordById(Long id);
    public List<SqDeviceRecord> selectSqDeviceRecordList(SqDeviceRecord record);
    public int insertSqDeviceRecord(SqDeviceRecord record);
    public int updateSqDeviceRecord(SqDeviceRecord record);
    public int deleteSqDeviceRecordByIds(Long[] ids);
}
