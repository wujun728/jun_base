package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SqChute;

public interface ISqChuteService
{
    public SqChute selectSqChuteById(Long id);

    public List<SqChute> selectSqChuteList(SqChute chute);

    public List<SqChute> selectSqChuteHeightList(SqChute chute);

    public List<SqChute> selectSqChuteOptions(SqChute chute);

    public int insertSqChute(SqChute chute);

    public int updateSqChute(SqChute chute);

    public int updateSqChuteHeight(SqChute chute);

    public int deleteSqChuteByIds(Long[] ids);
}
