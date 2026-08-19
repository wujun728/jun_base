package com.ruoyi.ai.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ai.mapper.ChatSessionMapper;
import com.ruoyi.ai.domain.ChatSession;
import com.ruoyi.ai.service.IChatSessionService;

/**
 * 聊天会话Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-04
 */
@Service
public class ChatSessionServiceImpl implements IChatSessionService
{
    @Autowired
    private ChatSessionMapper chatSessionMapper;

    /**
     * 查询聊天会话
     *
     * @param id 聊天会话主键
     * @return 聊天会话
     */
    @Override
    public ChatSession selectChatSessionById(Long id)
    {
        return chatSessionMapper.selectChatSessionById(id);
    }

    /**
     * 查询聊天会话列表
     *
     * @param chatSession 聊天会话
     * @return 聊天会话
     */
    @Override
    public List<ChatSession> selectChatSessionList(ChatSession chatSession)
    {
        return chatSessionMapper.selectChatSessionList(chatSession);
    }

    /**
     * 新增聊天会话
     *
     * @param chatSession 聊天会话
     * @return 结果
     */
    @Override
    public int insertChatSession(ChatSession chatSession)
    {
        chatSession.setUserId(SecurityUtils.getUserId());
        chatSession.setCreateTime(DateUtils.getNowDate());
        return chatSessionMapper.insertChatSession(chatSession);
    }

    /**
     * 修改聊天会话
     *
     * @param chatSession 聊天会话
     * @return 结果
     */
    @Override
    public int updateChatSession(ChatSession chatSession)
    {
        return chatSessionMapper.updateChatSession(chatSession);
    }

    /**
     * 批量删除聊天会话
     *
     * @param ids 需要删除的聊天会话主键
     * @return 结果
     */
    @Override
    public int deleteChatSessionByIds(Long[] ids)
    {
        return chatSessionMapper.deleteChatSessionByIds(ids);
    }

    /**
     * 删除聊天会话信息
     *
     * @param id 聊天会话主键
     * @return 结果
     */
    @Override
    public int deleteChatSessionById(Long id)
    {
        return chatSessionMapper.deleteChatSessionById(id);
    }
}
