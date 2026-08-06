package com.ruoyi.modelMessage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.modelMessage.domain.MessageVariable;
import com.ruoyi.modelMessage.mapper.MessageVariableMapper;
import com.ruoyi.modelMessage.service.IMessageVariableService;

/**
 * 变量管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-12-31
 */
@Service
public class MessageVariableServiceImpl implements IMessageVariableService 
{
    
    @Autowired
    private MessageVariableMapper messageVariableMapper;

    /**
     * 查询变量管理
     * 
     * @param variableId 变量管理主键
     * @return 变量管理
     */
    @Override
    public MessageVariable selectMessageVariableByVariableId(Long variableId)
    {
        return messageVariableMapper.selectMessageVariableByVariableId(variableId);
    }

    /**
     * 查询变量管理列表
     * 
     * @param messageVariable 变量管理
     * @return 变量管理
     */
    @Override
    public List<MessageVariable> selectMessageVariableList(MessageVariable messageVariable)
    {
        return messageVariableMapper.selectMessageVariableList(messageVariable);
    }

    /**
     * 新增变量管理
     * 
     * @param messageVariable 变量管理
     * @return 结果
     */
    @Override
    public int insertMessageVariable(MessageVariable messageVariable)
    {
        messageVariable.setCreateBy(SecurityUtils.getUsername());
        messageVariable.setCreateTime(DateUtils.getNowDate());
        messageVariable.setUpdateBy(SecurityUtils.getUsername());
        messageVariable.setUpdateTime(DateUtils.getNowDate());
        return messageVariableMapper.insertMessageVariable(messageVariable);
    }

    /**
     * 修改变量管理
     * 
     * @param messageVariable 变量管理
     * @return 结果
     */
    @Override
    public int updateMessageVariable(MessageVariable messageVariable)
    {
        messageVariable.setUpdateBy(SecurityUtils.getUsername());
        messageVariable.setUpdateTime(DateUtils.getNowDate());
        return messageVariableMapper.updateMessageVariable(messageVariable);
    }

    /**
     * 批量删除变量管理
     * 
     * @param variableIds 需要删除的变量管理主键
     * @return 结果
     */
    @Override
    public int deleteMessageVariableByVariableIds(Long[] variableIds)
    {
        for (Long variableId : variableIds) {
            // 获取变量信息
            MessageVariable variable = messageVariableMapper.selectMessageVariableByVariableId(variableId);
            if (variable == null) {
                throw new ServiceException("未找到该变量信息！");
            }
            // 检查变量是否被模板使用
            String variableName = variable.getVariableName();
            List<String> variables = messageVariableMapper.selectAllTemplateVariables();
            for (String templateVariable : variables) {
                String[] templateParts = templateVariable.split("/");
                for (String part : templateParts) {
                    if (part.equals(variableName)) {
                        throw new ServiceException("变量为 '" + variableName + "'' 已被模版使用，不能删除！");
                    }
                }
            }
        }
        return messageVariableMapper.deleteMessageVariableByVariableIds(variableIds);
    }

    /**
     * 删除变量管理信息
     * 
     * @param variableId 变量管理主键
     * @return 结果
     */
    @Override
    public int deleteMessageVariableByVariableId(Long variableId)
    {
        return messageVariableMapper.deleteMessageVariableByVariableId(variableId);
    }

    //查询变量
    @Override
    public List<MessageVariable> selectMessageVariable() {
       return messageVariableMapper.selectMessageVariable();
    }
}
