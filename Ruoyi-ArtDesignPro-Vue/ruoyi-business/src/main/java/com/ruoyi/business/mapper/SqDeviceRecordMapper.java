package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.SqDeviceRecord;

public interface SqDeviceRecordMapper
{
    public SqDeviceRecord selectSqDeviceRecordById(Long id);
    public List<SqDeviceRecord> selectSqDeviceRecordList(SqDeviceRecord record);
    public int insertSqDeviceRecord(SqDeviceRecord record);
    public int updateSqDeviceRecord(SqDeviceRecord record);
    public int deleteSqDeviceRecordByIds(Long[] ids);
}
