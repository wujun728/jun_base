package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SqSafetyAlert;

public interface ISqSafetyAlertService
{
    public SqSafetyAlert selectSqSafetyAlertById(Long id);
    public List<SqSafetyAlert> selectSqSafetyAlertList(SqSafetyAlert alert);
    public int insertSqSafetyAlert(SqSafetyAlert alert);
    public int updateSqSafetyAlert(SqSafetyAlert alert);
    public int deleteSqSafetyAlertByIds(Long[] ids);
}
