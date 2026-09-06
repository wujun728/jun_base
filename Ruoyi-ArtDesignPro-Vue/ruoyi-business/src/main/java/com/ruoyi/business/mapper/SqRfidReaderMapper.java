package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.SqRfidReader;

public interface SqRfidReaderMapper
{
    public SqRfidReader selectSqRfidReaderById(Long id);
    public List<SqRfidReader> selectSqRfidReaderList(SqRfidReader reader);
    public int insertSqRfidReader(SqRfidReader reader);
    public int updateSqRfidReader(SqRfidReader reader);
    public int deleteSqRfidReaderByIds(Long[] ids);
}
