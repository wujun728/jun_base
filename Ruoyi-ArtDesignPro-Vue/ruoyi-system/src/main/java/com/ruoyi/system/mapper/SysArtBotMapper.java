package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.SysArtBotConversation;
import com.ruoyi.system.domain.SysArtBotMessage;
import com.ruoyi.system.domain.SysArtBotModel;

public interface SysArtBotMapper
{
    List<SysArtBotModel> selectModelList(SysArtBotModel model);
    List<SysArtBotModel> selectEnabledModels();
    SysArtBotModel selectModelById(Long modelId);
    int insertModel(SysArtBotModel model);
    int updateModel(SysArtBotModel model);
    int clearDefaultModel(@Param("excludeModelId") Long excludeModelId);
    int deleteModel(Long modelId);
    int countConversationsByModelId(Long modelId);

    List<SysArtBotConversation> selectConversationList(Long userId);
    SysArtBotConversation selectConversation(@Param("conversationId") Long conversationId, @Param("userId") Long userId);
    int insertConversation(SysArtBotConversation conversation);
    int updateConversationTitle(@Param("conversationId") Long conversationId, @Param("title") String title);
    int touchConversation(Long conversationId);
    int deleteMessagesByConversationId(Long conversationId);
    int deleteConversation(@Param("conversationId") Long conversationId, @Param("userId") Long userId);

    List<SysArtBotMessage> selectMessageList(Long conversationId);
    List<SysArtBotMessage> selectRecentMessages(@Param("conversationId") Long conversationId, @Param("limit") int limit);
    int insertMessage(SysArtBotMessage message);
}
