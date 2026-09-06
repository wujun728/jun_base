package com.ruoyi.business.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.business.domain.SqChute;

public interface SqChuteMapper
{
    public SqChute selectSqChuteById(Long id);

    public List<SqChute> selectSqChuteList(SqChute chute);

    public List<SqChute> selectSqChuteHeightList(SqChute chute);

    public List<SqChute> selectSqChuteOptions(SqChute chute);

    public int insertSqChute(SqChute chute);

    public int updateSqChute(SqChute chute);

    public int updateSqChuteHeight(SqChute chute);

    public int deleteSqChuteByIds(Long[] ids);

    public int countSqChuteByIp(SqChute chute);

    public String selectDictLabel(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    public String selectDensityByLabel(@Param("dictLabel") String dictLabel);
}
