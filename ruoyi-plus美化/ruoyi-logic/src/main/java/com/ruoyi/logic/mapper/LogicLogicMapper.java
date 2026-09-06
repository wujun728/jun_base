package com.ruoyi.logic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.ruoyi.logic.domain.LogicLogic;

/**
 * 逻辑演示Mapper接口（MyBatis-Plus）
 * 
 * @author ruoyi
 * @date 2026-01-26
 */
public interface LogicLogicMapper extends BaseMapper<LogicLogic>
{
    public LogicLogic selectLogicLogicById(Long id);

    public List<LogicLogic> selectLogicLogicList(LogicLogic logicLogic);

    public int insertLogicLogic(LogicLogic logicLogic);

    public int updateLogicLogic(LogicLogic logicLogic);

    public int deleteLogicLogicById(Long id);

    public int deleteLogicLogicByIds(Long[] ids);
}
