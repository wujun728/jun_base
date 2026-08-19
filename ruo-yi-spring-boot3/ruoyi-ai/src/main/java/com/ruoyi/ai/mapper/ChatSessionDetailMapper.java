package com.ruoyi.ai.mapper;

import java.util.List;
import com.ruoyi.ai.domain.ChatSessionDetail;

/**
 * 聊天会话详情Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-04
 */
public interface ChatSessionDetailMapper 
{
    /**
     * 查询聊天会话详情
     * 
     * @param id 聊天会话详情主键
     * @return 聊天会话详情
     */
    public ChatSessionDetail selectChatSessionDetailById(Long id);

    /**
     * 查询聊天会话详情列表
     * 
     * @param chatSessionDetail 聊天会话详情
     * @return 聊天会话详情集合
     */
    public List<ChatSessionDetail> selectChatSessionDetailList(ChatSessionDetail chatSessionDetail);

    /**
     * 新增聊天会话详情
     * 
     * @param chatSessionDetail 聊天会话详情
     * @return 结果
     */
    public int insertChatSessionDetail(ChatSessionDetail chatSessionDetail);

    /**
     * 修改聊天会话详情
     * 
     * @param chatSessionDetail 聊天会话详情
     * @return 结果
     */
    public int updateChatSessionDetail(ChatSessionDetail chatSessionDetail);

    /**
     * 删除聊天会话详情
     * 
     * @param id 聊天会话详情主键
     * @return 结果
     */
    public int deleteChatSessionDetailById(Long id);

    /**
     * 批量删除聊天会话详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChatSessionDetailByIds(Long[] ids);
}
