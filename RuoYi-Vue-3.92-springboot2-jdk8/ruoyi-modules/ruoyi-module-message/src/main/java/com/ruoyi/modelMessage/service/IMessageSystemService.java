package com.ruoyi.modelMessage.service;

import java.util.List;

import com.ruoyi.modelMessage.domain.MessageSystem;

/**
 * 消息管理Service接口
 * 
 * @author ruoyi
 * @date 2024-12-21
 */
public interface IMessageSystemService 
{
    /**
     * 查询消息管理
     * 
     * @param messageId 消息管理主键
     * @return 消息管理
     */
    public MessageSystem selectMessageSystemByMessageId(Long messageId);

    /**
     * 查询消息管理列表
     * 
     * @param messageSystem 消息管理
     * @return 消息管理集合
     */
    public List<MessageSystem> selectMessageSystemList(MessageSystem messageSystem);

    /**
     * 新增消息管理
     * 
     * @param messageSystem 消息管理
     * @return 结果
     */
    public int insertMessageSystem(MessageSystem messageSystem);

    /**
     * 修改消息管理
     * 
     * @param messageSystem 消息管理
     * @return 结果
     */
    public int updateMessageSystem(MessageSystem messageSystem);

    /**
     * 批量删除消息管理
     * 
     * @param messageIds 需要删除的消息管理主键集合
     * @return 结果
     */
    public int deleteMessageSystemByMessageIds(Long[] messageIds);

    /**
     * 删除消息管理信息
     * 
     * @param messageId 消息管理主键
     * @return 结果
     */
    public int deleteMessageSystemByMessageId(Long messageId);

    //点击信息详情状态调整为已读
    public int updateState(Long messageId);

    //根据发送方式 执行不同操作
    public void processMessageSystem(MessageSystem messageSystem);

    // 批量发送信息
    public int batchInsertMessageSystem(List<MessageSystem> messageSystemList);

     /**
     * 统一查询系统资源信息（角色、部门、用户）
     *
     * @param type     查询类型：role-角色信息, dept-部门信息, user-用户信息, usersbyrole-根据角色查用户, usersbydept-根据部门查用户, usersbysendmode-根据发送方式查用户
     * @param id       可选参数，当查询特定角色或部门下的用户时使用
     * @param sendMode 可选参数，当查询特定发送方式的用户时使用（phone/email）
     * @return 查询结果
     */
    public Object querySystemResource(String type, Long id, String sendMode);
}
