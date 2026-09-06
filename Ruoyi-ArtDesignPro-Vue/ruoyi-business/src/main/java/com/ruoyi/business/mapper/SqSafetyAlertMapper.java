package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.SqSafetyAlert;

public interface SqSafetyAlertMapper
{
    public SqSafetyAlert selectSqSafetyAlertById(Long id);
    public List<SqSafetyAlert> selectSqSafetyAlertList(SqSafetyAlert alert);
    public int insertSqSafetyAlert(SqSafetyAlert alert);
    public int updateSqSafetyAlert(SqSafetyAlert alert);
    public int deleteSqSafetyAlertByIds(Long[] ids);
}
