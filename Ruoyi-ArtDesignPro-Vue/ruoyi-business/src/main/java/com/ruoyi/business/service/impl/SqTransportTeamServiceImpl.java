package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqTransportTeam;
import com.ruoyi.business.mapper.SqTransportTeamMapper;
import com.ruoyi.business.service.ISqTransportTeamService;
import com.ruoyi.common.utils.StringUtils;

@Service
public class SqTransportTeamServiceImpl implements ISqTransportTeamService
{
    @Autowired
    private SqTransportTeamMapper sqTransportTeamMapper;

    @Override
    public SqTransportTeam selectSqTransportTeamById(Long id)
    {
        return sqTransportTeamMapper.selectSqTransportTeamById(id);
    }

    @Override
    public List<SqTransportTeam> selectSqTransportTeamList(SqTransportTeam team)
    {
        normalizeDeletedFlag(team);
        return sqTransportTeamMapper.selectSqTransportTeamList(team);
    }

    @Override
    public List<SqTransportTeam> selectSqTransportTeamOptions(SqTransportTeam team)
    {
        normalizeDeletedFlag(team);
        return sqTransportTeamMapper.selectSqTransportTeamOptions(team);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSqTransportTeam(SqTransportTeam team)
    {
        normalizeDeletedFlag(team);
        if (StringUtils.isBlank(team.getStatus()))
        {
            team.setStatus("0");
        }
        return sqTransportTeamMapper.insertSqTransportTeam(team);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSqTransportTeam(SqTransportTeam team)
    {
        return sqTransportTeamMapper.updateSqTransportTeam(team);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqTransportTeamByIds(Long[] ids)
    {
        return sqTransportTeamMapper.deleteSqTransportTeamByIds(ids);
    }

    private void normalizeDeletedFlag(SqTransportTeam team)
    {
        if (StringUtils.isBlank(team.getIsDeleted()))
        {
            team.setIsDeleted("0");
        }
    }
}
