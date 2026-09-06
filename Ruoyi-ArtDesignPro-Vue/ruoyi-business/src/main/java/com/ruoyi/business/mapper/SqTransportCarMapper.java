package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.SqTransportCar;

public interface SqTransportCarMapper
{
    public SqTransportCar selectSqTransportCarById(Long id);
    public List<SqTransportCar> selectSqTransportCarList(SqTransportCar car);
    public int insertSqTransportCar(SqTransportCar car);
    public int updateSqTransportCar(SqTransportCar car);
    public int deleteSqTransportCarByIds(Long[] ids);
}
