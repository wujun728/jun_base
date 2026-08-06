package com.ruoyi.web.demo.twotable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.demo.twotable.entity.Car;
import com.ruoyi.web.demo.twotable.entity.CarModel;
import com.ruoyi.web.demo.twotable.mapper.CarMapper;
import com.ruoyi.web.demo.twotable.service.ICarModelService;
import com.ruoyi.web.demo.twotable.service.ICarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

@Transactional
@Service("carService")
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements ICarService {
    @Autowired
    private ICarModelService carModelService;

    @Override
    public boolean removeById(Serializable id) {
        carModelService.remove(new QueryWrapper<CarModel>().eq("car_id",id));
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<?> idList) {
        for (Object id : idList) {
            super.removeById((java.io.Serializable) id);
        }
        return true;
    }
}
