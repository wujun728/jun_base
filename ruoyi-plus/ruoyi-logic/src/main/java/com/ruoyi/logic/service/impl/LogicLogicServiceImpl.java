package com.ruoyi.logic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.logic.domain.LogicLogic;
import com.ruoyi.logic.mapper.LogicLogicMapper;
import com.ruoyi.logic.service.ILogicLogicService;

@Service
public class LogicLogicServiceImpl extends ServiceImpl<LogicLogicMapper, LogicLogic> implements ILogicLogicService {
    @Override
    public LogicLogic selectLogicLogicById(Long id) {
        return this.baseMapper.selectLogicLogicById(id);
    }

    @Override
    public List<LogicLogic> selectLogicLogicList(LogicLogic logicLogic) {
        return this.baseMapper.selectLogicLogicList(logicLogic);
    }

    @Override
    public int insertLogicLogic(LogicLogic logicLogic) {
        logicLogic.setCreateTime(DateUtils.getNowDate());
        return this.baseMapper.insertLogicLogic(logicLogic);
    }

    @Override
    public int updateLogicLogic(LogicLogic logicLogic) {
        logicLogic.setUpdateTime(DateUtils.getNowDate());
        return this.baseMapper.updateLogicLogic(logicLogic);
    }

    @Override
    public int deleteLogicLogicByIds(Long[] ids) {
        return this.baseMapper.deleteLogicLogicByIds(ids);
    }

    @Override
    public int deleteLogicLogicById(Long id) {
        return this.baseMapper.deleteLogicLogicById(id);
    }
}
