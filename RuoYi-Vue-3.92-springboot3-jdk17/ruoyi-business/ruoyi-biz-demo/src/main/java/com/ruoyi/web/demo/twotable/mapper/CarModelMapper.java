package com.ruoyi.web.demo.twotable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.demo.twotable.entity.CarModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CarModelMapper extends BaseMapper<CarModel> {
    List<CarModel> selectDictList();
}
