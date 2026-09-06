package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqBehaviorMonitor;
import com.ruoyi.business.mapper.SqUserBehaviorMapper;
import com.ruoyi.business.service.ISqUserBehaviorService;
import com.ruoyi.common.utils.StringUtils;

@Service
public class SqUserBehaviorServiceImpl implements ISqUserBehaviorService
{
    @Autowired
    private SqUserBehaviorMapper sqUserBehaviorMapper;

    public SqBehaviorMonitor selectSqUserBehaviorById(Long id) { return sqUserBehaviorMapper.selectSqUserBehaviorById(id); }
    public List<SqBehaviorMonitor> selectSqUserBehaviorList(SqBehaviorMonitor behavior) { return sqUserBehaviorMapper.selectSqUserBehaviorList(behavior); }
    @Transactional(rollbackFor = Exception.class)
    public int insertSqUserBehavior(SqBehaviorMonitor behavior) { normalize(behavior); return sqUserBehaviorMapper.insertSqUserBehavior(behavior); }
    @Transactional(rollbackFor = Exception.class)
    public int updateSqUserBehavior(SqBehaviorMonitor behavior) { return sqUserBehaviorMapper.updateSqUserBehavior(behavior); }
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqUserBehaviorByIds(Long[] ids) { return sqUserBehaviorMapper.deleteSqUserBehaviorByIds(ids); }

    private void normalize(SqBehaviorMonitor behavior)
    {
        if (StringUtils.isBlank(behavior.getStatus())) behavior.setStatus("0");
    }
}
