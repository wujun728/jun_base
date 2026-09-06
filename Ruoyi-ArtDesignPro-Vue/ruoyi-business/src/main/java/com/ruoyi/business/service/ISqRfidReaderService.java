package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SqRfidReader;

public interface ISqRfidReaderService
{
    public SqRfidReader selectSqRfidReaderById(Long id);
    public List<SqRfidReader> selectSqRfidReaderList(SqRfidReader reader);
    public int insertSqRfidReader(SqRfidReader reader);
    public int updateSqRfidReader(SqRfidReader reader);
    public int deleteSqRfidReaderByIds(Long[] ids);
}
