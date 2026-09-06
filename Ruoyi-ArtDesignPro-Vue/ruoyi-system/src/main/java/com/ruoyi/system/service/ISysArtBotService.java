package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysArtBotConversation;
import com.ruoyi.system.domain.SysArtBotMessage;
import com.ruoyi.system.domain.SysArtBotModel;

public interface ISysArtBotService
{
    List<SysArtBotModel> selectModelList(SysArtBotModel model);
    List<SysArtBotModel> selectEnabledModels();
    SysArtBotModel selectModelById(Long modelId);
    SysArtBotModel selectModelSecretById(Long modelId);
    int insertModel(SysArtBotModel model);
    int updateModel(SysArtBotModel model);
    int setDefaultModel(Long modelId, String username);
    int deleteModel(Long modelId);

    List<SysArtBotConversation> selectConversationList(Long userId);
    SysArtBotConversation selectConversation(Long conversationId, Long userId);
    SysArtBotConversation createConversation(Long userId, Long modelId);
    void deleteConversation(Long conversationId, Long userId);
    List<SysArtBotMessage> selectMessageList(Long conversationId, Long userId);
    List<SysArtBotMessage> selectRecentMessages(Long conversationId, int limit);
    void insertMessage(Long conversationId, String role, String content);
    void titleConversation(Long conversationId, String content);
}
