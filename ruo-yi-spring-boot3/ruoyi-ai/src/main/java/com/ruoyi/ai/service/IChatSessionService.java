package com.ruoyi.ai.service;

import java.util.List;
import com.ruoyi.ai.domain.ChatSession;

/**
 * 聊天会话Service接口
 * 
 * @author ruoyi
 * @date 2025-03-04
 */
public interface IChatSessionService 
{
    /**
     * 查询聊天会话
     * 
     * @param id 聊天会话主键
     * @return 聊天会话
     */
    public ChatSession selectChatSessionById(Long id);

    /**
     * 查询聊天会话列表
     * 
     * @param chatSession 聊天会话
     * @return 聊天会话集合
     */
    public List<ChatSession> selectChatSessionList(ChatSession chatSession);

    /**
     * 新增聊天会话
     * 
     * @param chatSession 聊天会话
     * @return 结果
     */
    public int insertChatSession(ChatSession chatSession);

    /**
     * 修改聊天会话
     * 
     * @param chatSession 聊天会话
     * @return 结果
     */
    public int updateChatSession(ChatSession chatSession);

    /**
     * 批量删除聊天会话
     * 
     * @param ids 需要删除的聊天会话主键集合
     * @return 结果
     */
    public int deleteChatSessionByIds(Long[] ids);

    /**
     * 删除聊天会话信息
     * 
     * @param id 聊天会话主键
     * @return 结果
     */
    public int deleteChatSessionById(Long id);
}
