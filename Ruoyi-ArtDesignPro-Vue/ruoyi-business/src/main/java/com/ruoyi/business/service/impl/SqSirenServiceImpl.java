package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqSiren;
import com.ruoyi.business.mapper.SqSirenMapper;
import com.ruoyi.business.service.ISqSirenService;
import com.ruoyi.common.utils.StringUtils;

@Service
public class SqSirenServiceImpl implements ISqSirenService
{
    @Autowired
    private SqSirenMapper sqSirenMapper;

    public SqSiren selectSqSirenById(Long id) { return sqSirenMapper.selectSqSirenById(id); }
    public List<SqSiren> selectSqSirenList(SqSiren siren) { return sqSirenMapper.selectSqSirenList(siren); }
    @Transactional(rollbackFor = Exception.class)
    public int insertSqSiren(SqSiren siren) { normalize(siren); return sqSirenMapper.insertSqSiren(siren); }
    @Transactional(rollbackFor = Exception.class)
    public int updateSqSiren(SqSiren siren) { return sqSirenMapper.updateSqSiren(siren); }
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqSirenByIds(Long[] ids) { return sqSirenMapper.deleteSqSirenByIds(ids); }

    private void normalize(SqSiren siren)
    {
        if (StringUtils.isBlank(siren.getFlag())) siren.setFlag("0");
        if (StringUtils.isBlank(siren.getIsWarning())) siren.setIsWarning("0");
    }
}
