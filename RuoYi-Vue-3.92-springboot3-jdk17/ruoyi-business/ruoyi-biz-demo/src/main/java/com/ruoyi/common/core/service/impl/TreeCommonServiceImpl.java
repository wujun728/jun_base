package com.ruoyi.common.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.TreeEntity;
import com.ruoyi.common.core.mapper.BaseTreeMapper;
import com.ruoyi.common.core.service.ITreeCommonService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class TreeCommonServiceImpl<M extends BaseTreeMapper<T>, T extends TreeEntity>
        extends ServiceImpl<M, T> implements ITreeCommonService<T> {

    @Override
    public T getById(Serializable id) {
        return baseMapper.selectByTreeId(id);
    }

    @Override
    public List<T> selectTreeList(Wrapper<T> wrapper) {
        return baseMapper.selectTreeList(wrapper);
    }
}
