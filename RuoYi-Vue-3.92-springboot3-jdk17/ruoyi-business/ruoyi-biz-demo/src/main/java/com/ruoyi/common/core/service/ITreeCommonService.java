package com.ruoyi.common.core.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.TreeEntity;
import java.util.List;

public interface ITreeCommonService<T extends TreeEntity> extends IService<T> {
    List<T> selectTreeList(Wrapper<T> wrapper);
}
