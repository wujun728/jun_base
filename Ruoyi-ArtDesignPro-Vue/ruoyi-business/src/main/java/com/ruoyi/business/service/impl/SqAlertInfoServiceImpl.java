package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqAlertInfo;
import com.ruoyi.business.mapper.SqAlertInfoMapper;
import com.ruoyi.business.service.ISqAlertInfoService;
import com.ruoyi.common.utils.StringUtils;

@Service
public class SqAlertInfoServiceImpl implements ISqAlertInfoService
{
    @Autowired
    private SqAlertInfoMapper sqAlertInfoMapper;

    public SqAlertInfo selectSqAlertInfoById(Long id) { return sqAlertInfoMapper.selectSqAlertInfoById(id); }
    public List<SqAlertInfo> selectSqAlertInfoList(SqAlertInfo info) { normalize(info); return sqAlertInfoMapper.selectSqAlertInfoList(info); }
    @Transactional(rollbackFor = Exception.class)
    public int insertSqAlertInfo(SqAlertInfo info) { normalize(info); return sqAlertInfoMapper.insertSqAlertInfo(info); }
    @Transactional(rollbackFor = Exception.class)
    public int updateSqAlertInfo(SqAlertInfo info) { return sqAlertInfoMapper.updateSqAlertInfo(info); }
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqAlertInfoByIds(Long[] ids) { return sqAlertInfoMapper.deleteSqAlertInfoByIds(ids); }

    private void normalize(SqAlertInfo info)
    {
        if (StringUtils.isBlank(info.getIsDeleted())) info.setIsDeleted("0");
        if (StringUtils.isBlank(info.getStatus())) info.setStatus("0");
    }
}
