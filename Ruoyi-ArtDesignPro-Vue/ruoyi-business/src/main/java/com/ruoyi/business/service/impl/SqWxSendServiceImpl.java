package com.ruoyi.business.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqWxSend;
import com.ruoyi.business.mapper.SqWxSendMapper;
import com.ruoyi.business.service.ISqWxSendService;
import com.ruoyi.common.utils.StringUtils;

@Service
public class SqWxSendServiceImpl implements ISqWxSendService
{
    private static final AtomicInteger ID_SEQUENCE = new AtomicInteger(0);

    @Autowired
    private SqWxSendMapper sqWxSendMapper;

    @Override
    public SqWxSend selectSqWxSendById(Long id) { return sqWxSendMapper.selectSqWxSendById(id); }
    @Override
    public List<SqWxSend> selectSqWxSendList(SqWxSend send) { return sqWxSendMapper.selectSqWxSendList(send); }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSqWxSend(SqWxSend send)
    {
        if (send.getId() == null)
        {
            send.setId(nextSafeLongId());
        }
        if (StringUtils.isBlank(send.getIsSend()))
        {
            send.setIsSend("0");
        }
        return sqWxSendMapper.insertSqWxSend(send);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSqWxSend(SqWxSend send) { return sqWxSendMapper.updateSqWxSend(send); }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqWxSendByIds(Long[] ids) { return sqWxSendMapper.deleteSqWxSendByIds(ids); }

    private Long nextSafeLongId()
    {
        int sequence = ID_SEQUENCE.updateAndGet(value -> value >= 999 ? 0 : value + 1);
        return System.currentTimeMillis() * 1000L + sequence;
    }
}
