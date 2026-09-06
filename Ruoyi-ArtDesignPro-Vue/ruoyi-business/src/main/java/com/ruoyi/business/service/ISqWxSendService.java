package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SqWxSend;

public interface ISqWxSendService
{
    public SqWxSend selectSqWxSendById(Long id);
    public List<SqWxSend> selectSqWxSendList(SqWxSend send);
    public int insertSqWxSend(SqWxSend send);
    public int updateSqWxSend(SqWxSend send);
    public int deleteSqWxSendByIds(Long[] ids);
}
