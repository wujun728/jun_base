package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqRfidReader;
import com.ruoyi.business.mapper.SqRfidReaderMapper;
import com.ruoyi.business.service.ISqRfidReaderService;
import com.ruoyi.common.utils.StringUtils;

@Service
public class SqRfidReaderServiceImpl implements ISqRfidReaderService
{
    @Autowired
    private SqRfidReaderMapper sqRfidReaderMapper;

    @Override
    public SqRfidReader selectSqRfidReaderById(Long id)
    {
        return sqRfidReaderMapper.selectSqRfidReaderById(id);
    }

    @Override
    public List<SqRfidReader> selectSqRfidReaderList(SqRfidReader reader)
    {
        return sqRfidReaderMapper.selectSqRfidReaderList(reader);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSqRfidReader(SqRfidReader reader)
    {
        if (StringUtils.isBlank(reader.getStatus()))
        {
            reader.setStatus("运行");
        }
        return sqRfidReaderMapper.insertSqRfidReader(reader);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSqRfidReader(SqRfidReader reader)
    {
        return sqRfidReaderMapper.updateSqRfidReader(reader);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqRfidReaderByIds(Long[] ids)
    {
        return sqRfidReaderMapper.deleteSqRfidReaderByIds(ids);
    }
}
