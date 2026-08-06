package com.ruoyi.modelMessage.service;

import java.util.List;

import com.ruoyi.modelMessage.domain.MessageVariable;

/**
 * 变量管理Service接口
 * 
 * @author ruoyi
 * @date 2024-12-31
 */
public interface IMessageVariableService 
{
    /**
     * 查询变量管理
     * 
     * @param variableId 变量管理主键
     * @return 变量管理
     */
    public MessageVariable selectMessageVariableByVariableId(Long variableId);

    /**
     * 查询变量管理列表
     * 
     * @param messageVariable 变量管理
     * @return 变量管理集合
     */
    public List<MessageVariable> selectMessageVariableList(MessageVariable messageVariable);

    /**
     * 新增变量管理
     * 
     * @param messageVariable 变量管理
     * @return 结果
     */
    public int insertMessageVariable(MessageVariable messageVariable);

    /**
     * 修改变量管理
     * 
     * @param messageVariable 变量管理
     * @return 结果
     */
    public int updateMessageVariable(MessageVariable messageVariable);

    /**
     * 批量删除变量管理
     * 
     * @param variableIds 需要删除的变量管理主键集合
     * @return 结果
     */
    public int deleteMessageVariableByVariableIds(Long[] variableIds);

    /**
     * 删除变量管理信息
     * 
     * @param variableId 变量管理主键
     * @return 结果
     */
    public int deleteMessageVariableByVariableId(Long variableId);

    /**
     *  查询全部变量
     */
    public List<MessageVariable> selectMessageVariable();
}
