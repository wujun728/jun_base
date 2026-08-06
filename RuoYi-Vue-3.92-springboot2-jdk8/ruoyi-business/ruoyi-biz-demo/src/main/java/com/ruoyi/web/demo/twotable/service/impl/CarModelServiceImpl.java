package com.ruoyi.web.demo.twotable.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.demo.twotable.entity.CarModel;
import com.ruoyi.web.demo.twotable.mapper.CarModelMapper;
import com.ruoyi.web.demo.twotable.service.ICarModelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("carModelService")
public class CarModelServiceImpl extends ServiceImpl<CarModelMapper, CarModel> implements ICarModelService {

    @Override
    public List<CarModel> selectDictList() {
        return baseMapper.selectDictList();
    }

}
