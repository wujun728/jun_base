package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SqTransportCar;

public interface ISqTransportCarService
{
    public SqTransportCar selectSqTransportCarById(Long id);
    public List<SqTransportCar> selectSqTransportCarList(SqTransportCar car);
    public int insertSqTransportCar(SqTransportCar car);
    public int updateSqTransportCar(SqTransportCar car);
    public int deleteSqTransportCarByIds(Long[] ids);
}
