package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqTransportCar;
import com.ruoyi.business.mapper.SqTransportCarMapper;
import com.ruoyi.business.service.ISqTransportCarService;
import com.ruoyi.common.utils.StringUtils;

@Service
public class SqTransportCarServiceImpl implements ISqTransportCarService
{
    @Autowired
    private SqTransportCarMapper sqTransportCarMapper;

    @Override
    public SqTransportCar selectSqTransportCarById(Long id)
    {
        return sqTransportCarMapper.selectSqTransportCarById(id);
    }

    @Override
    public List<SqTransportCar> selectSqTransportCarList(SqTransportCar car)
    {
        normalizeDeletedFlag(car);
        return sqTransportCarMapper.selectSqTransportCarList(car);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSqTransportCar(SqTransportCar car)
    {
        normalizeDeletedFlag(car);
        if (StringUtils.isBlank(car.getStatus()))
        {
            car.setStatus("0");
        }
        return sqTransportCarMapper.insertSqTransportCar(car);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSqTransportCar(SqTransportCar car)
    {
        return sqTransportCarMapper.updateSqTransportCar(car);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqTransportCarByIds(Long[] ids)
    {
        return sqTransportCarMapper.deleteSqTransportCarByIds(ids);
    }

    private void normalizeDeletedFlag(SqTransportCar car)
    {
        if (StringUtils.isBlank(car.getIsDeleted()))
        {
            car.setIsDeleted("0");
        }
    }
}
