package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqSafetyAlert;
import com.ruoyi.business.mapper.SqSafetyAlertMapper;
import com.ruoyi.business.service.ISqSafetyAlertService;
import com.ruoyi.common.utils.StringUtils;

@Service
public class SqSafetyAlertServiceImpl implements ISqSafetyAlertService
{
    @Autowired
    private SqSafetyAlertMapper sqSafetyAlertMapper;

    public SqSafetyAlert selectSqSafetyAlertById(Long id) { return sqSafetyAlertMapper.selectSqSafetyAlertById(id); }
    public List<SqSafetyAlert> selectSqSafetyAlertList(SqSafetyAlert alert) { normalize(alert); return sqSafetyAlertMapper.selectSqSafetyAlertList(alert); }
    @Transactional(rollbackFor = Exception.class)
    public int insertSqSafetyAlert(SqSafetyAlert alert) { normalize(alert); return sqSafetyAlertMapper.insertSqSafetyAlert(alert); }
    @Transactional(rollbackFor = Exception.class)
    public int updateSqSafetyAlert(SqSafetyAlert alert) { return sqSafetyAlertMapper.updateSqSafetyAlert(alert); }
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqSafetyAlertByIds(Long[] ids) { return sqSafetyAlertMapper.deleteSqSafetyAlertByIds(ids); }

    private void normalize(SqSafetyAlert alert)
    {
        if (StringUtils.isBlank(alert.getIsDeleted())) alert.setIsDeleted("0");
        if (StringUtils.isBlank(alert.getState())) alert.setState("0");
    }
}
