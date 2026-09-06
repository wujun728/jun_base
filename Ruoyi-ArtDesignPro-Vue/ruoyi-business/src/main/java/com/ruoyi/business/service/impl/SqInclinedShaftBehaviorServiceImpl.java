package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqBehaviorMonitor;
import com.ruoyi.business.mapper.SqInclinedShaftBehaviorMapper;
import com.ruoyi.business.service.ISqInclinedShaftBehaviorService;
import com.ruoyi.common.utils.StringUtils;

@Service
public class SqInclinedShaftBehaviorServiceImpl implements ISqInclinedShaftBehaviorService
{
    @Autowired
    private SqInclinedShaftBehaviorMapper sqInclinedShaftBehaviorMapper;

    public SqBehaviorMonitor selectSqInclinedShaftBehaviorById(Long id) { return sqInclinedShaftBehaviorMapper.selectSqInclinedShaftBehaviorById(id); }
    public List<SqBehaviorMonitor> selectSqInclinedShaftBehaviorList(SqBehaviorMonitor behavior) { return sqInclinedShaftBehaviorMapper.selectSqInclinedShaftBehaviorList(behavior); }
    @Transactional(rollbackFor = Exception.class)
    public int insertSqInclinedShaftBehavior(SqBehaviorMonitor behavior) { normalize(behavior); return sqInclinedShaftBehaviorMapper.insertSqInclinedShaftBehavior(behavior); }
    @Transactional(rollbackFor = Exception.class)
    public int updateSqInclinedShaftBehavior(SqBehaviorMonitor behavior) { return sqInclinedShaftBehaviorMapper.updateSqInclinedShaftBehavior(behavior); }
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqInclinedShaftBehaviorByIds(Long[] ids) { return sqInclinedShaftBehaviorMapper.deleteSqInclinedShaftBehaviorByIds(ids); }

    private void normalize(SqBehaviorMonitor behavior)
    {
        if (StringUtils.isBlank(behavior.getStatus())) behavior.setStatus("0");
    }
}
