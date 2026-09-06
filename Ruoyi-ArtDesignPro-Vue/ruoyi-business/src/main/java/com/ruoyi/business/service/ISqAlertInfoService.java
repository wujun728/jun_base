package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SqAlertInfo;

public interface ISqAlertInfoService
{
    public SqAlertInfo selectSqAlertInfoById(Long id);
    public List<SqAlertInfo> selectSqAlertInfoList(SqAlertInfo info);
    public int insertSqAlertInfo(SqAlertInfo info);
    public int updateSqAlertInfo(SqAlertInfo info);
    public int deleteSqAlertInfoByIds(Long[] ids);
}
