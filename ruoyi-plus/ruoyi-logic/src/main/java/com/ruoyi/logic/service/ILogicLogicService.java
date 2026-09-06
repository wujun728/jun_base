package com.ruoyi.logic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.ruoyi.logic.domain.LogicLogic;
public interface ILogicLogicService extends IService<LogicLogic>
{
    public LogicLogic selectLogicLogicById(Long id);
    public List<LogicLogic> selectLogicLogicList(LogicLogic logicLogic);
    public int insertLogicLogic(LogicLogic logicLogic);
    public int updateLogicLogic(LogicLogic logicLogic);
    public int deleteLogicLogicByIds(Long[] ids);
    public int deleteLogicLogicById(Long id);
}
