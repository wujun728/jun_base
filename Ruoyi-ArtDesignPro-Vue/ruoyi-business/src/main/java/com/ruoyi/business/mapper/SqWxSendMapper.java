package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.SqWxSend;

public interface SqWxSendMapper
{
    public SqWxSend selectSqWxSendById(Long id);
    public List<SqWxSend> selectSqWxSendList(SqWxSend send);
    public int insertSqWxSend(SqWxSend send);
    public int updateSqWxSend(SqWxSend send);
    public int deleteSqWxSendByIds(Long[] ids);
}
