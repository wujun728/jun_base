package com.ruoyi.ai.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ai.mapper.ChatSessionDetailMapper;
import com.ruoyi.ai.domain.ChatSessionDetail;
import com.ruoyi.ai.service.IChatSessionDetailService;

/**
 * 聊天会话详情Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-04
 */
@Service
public class ChatSessionDetailServiceImpl implements IChatSessionDetailService 
{
    @Autowired
    private ChatSessionDetailMapper chatSessionDetailMapper;

    /**
     * 查询聊天会话详情
     * 
     * @param id 聊天会话详情主键
     * @return 聊天会话详情
     */
    @Override
    public ChatSessionDetail selectChatSessionDetailById(Long id)
    {
        return chatSessionDetailMapper.selectChatSessionDetailById(id);
    }

    /**
     * 查询聊天会话详情列表
     * 
     * @param chatSessionDetail 聊天会话详情
     * @return 聊天会话详情
     */
    @Override
    public List<ChatSessionDetail> selectChatSessionDetailList(ChatSessionDetail chatSessionDetail)
    {
        return chatSessionDetailMapper.selectChatSessionDetailList(chatSessionDetail);
    }

    /**
     * 新增聊天会话详情
     * 
     * @param chatSessionDetail 聊天会话详情
     * @return 结果
     */
    @Override
    public int insertChatSessionDetail(ChatSessionDetail chatSessionDetail)
    {
        chatSessionDetail.setCreateTime(DateUtils.getNowDate());
        return chatSessionDetailMapper.insertChatSessionDetail(chatSessionDetail);
    }

    /**
     * 修改聊天会话详情
     * 
     * @param chatSessionDetail 聊天会话详情
     * @return 结果
     */
    @Override
    public int updateChatSessionDetail(ChatSessionDetail chatSessionDetail)
    {
        return chatSessionDetailMapper.updateChatSessionDetail(chatSessionDetail);
    }

    /**
     * 批量删除聊天会话详情
     * 
     * @param ids 需要删除的聊天会话详情主键
     * @return 结果
     */
    @Override
    public int deleteChatSessionDetailByIds(Long[] ids)
    {
        return chatSessionDetailMapper.deleteChatSessionDetailByIds(ids);
    }

    /**
     * 删除聊天会话详情信息
     * 
     * @param id 聊天会话详情主键
     * @return 结果
     */
    @Override
    public int deleteChatSessionDetailById(Long id)
    {
        return chatSessionDetailMapper.deleteChatSessionDetailById(id);
    }
}
