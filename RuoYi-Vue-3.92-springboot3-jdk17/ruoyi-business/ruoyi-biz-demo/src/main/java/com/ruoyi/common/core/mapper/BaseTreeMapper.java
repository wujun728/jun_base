package com.ruoyi.common.core.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;

public interface BaseTreeMapper<T> extends BaseMapper<T> {
    T selectByTreeId(Serializable id);
    List<T> selectTreeList(@Param("ew") Wrapper<T> wrapper);
    void updateSunTreeParentIds(@Param("newParentIds") String newParentIds, @Param("oldParentIds") String oldParentIds);
    void deleteSunTree(String parentIds);
}
