package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.SqTransportTeam;

public interface SqTransportTeamMapper
{
    public SqTransportTeam selectSqTransportTeamById(Long id);
    public List<SqTransportTeam> selectSqTransportTeamList(SqTransportTeam team);
    public List<SqTransportTeam> selectSqTransportTeamOptions(SqTransportTeam team);
    public int insertSqTransportTeam(SqTransportTeam team);
    public int updateSqTransportTeam(SqTransportTeam team);
    public int deleteSqTransportTeamByIds(Long[] ids);
}
