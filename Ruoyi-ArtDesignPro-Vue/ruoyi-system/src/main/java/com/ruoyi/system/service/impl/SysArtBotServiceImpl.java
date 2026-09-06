package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysArtBotConversation;
import com.ruoyi.system.domain.SysArtBotMessage;
import com.ruoyi.system.domain.SysArtBotModel;
import com.ruoyi.system.mapper.SysArtBotMapper;
import com.ruoyi.system.service.ISysArtBotService;

@Service
public class SysArtBotServiceImpl implements ISysArtBotService
{
    private static final String MASKED_KEY = "********";

    @Autowired
    private SysArtBotMapper mapper;

    @Override
    public List<SysArtBotModel> selectModelList(SysArtBotModel model)
    {
        List<SysArtBotModel> models = mapper.selectModelList(model);
        models.forEach(this::maskApiKey);
        return models;
    }

    @Override
    public List<SysArtBotModel> selectEnabledModels()
    {
        List<SysArtBotModel> models = mapper.selectEnabledModels();
        models.forEach(this::maskApiKey);
        return models;
    }

    @Override
    public SysArtBotModel selectModelById(Long modelId)
    {
        SysArtBotModel model = mapper.selectModelById(modelId);
        maskApiKey(model);
        return model;
    }

    @Override
    public SysArtBotModel selectModelSecretById(Long modelId)
    {
        return mapper.selectModelById(modelId);
    }

    @Override
    @Transactional
    public int insertModel(SysArtBotModel model)
    {
        normalize(model);
        if ("1".equals(model.getIsDefault()))
        {
            mapper.clearDefaultModel(null);
        }
        return mapper.insertModel(model);
    }

    @Override
    @Transactional
    public int updateModel(SysArtBotModel model)
    {
        SysArtBotModel current = requiredModel(model.getModelId());
        normalize(model);
        if (MASKED_KEY.equals(model.getApiKey()))
        {
            model.setApiKey(null);
        }
        if ("1".equals(current.getIsDefault()) && "1".equals(model.getStatus()))
        {
            throw new ServiceException("默认模型不能停用");
        }
        if ("1".equals(model.getIsDefault()))
        {
            mapper.clearDefaultModel(model.getModelId());
        }
        return mapper.updateModel(model);
    }

    @Override
    @Transactional
    public int setDefaultModel(Long modelId, String username)
    {
        SysArtBotModel model = requiredModel(modelId);
        if (!"0".equals(model.getStatus()))
        {
            throw new ServiceException("停用模型不能设为默认模型");
        }
        mapper.clearDefaultModel(modelId);
        SysArtBotModel update = new SysArtBotModel();
        update.setModelId(modelId);
        update.setIsDefault("1");
        update.setUpdateBy(username);
        return mapper.updateModel(update);
    }

    @Override
    public int deleteModel(Long modelId)
    {
        SysArtBotModel model = requiredModel(modelId);
        if ("1".equals(model.getIsDefault()))
        {
            throw new ServiceException("默认模型不能删除");
        }
        if (mapper.countConversationsByModelId(modelId) > 0)
        {
            throw new ServiceException("模型已有聊天会话，不能删除");
        }
        return mapper.deleteModel(modelId);
    }

    @Override
    public List<SysArtBotConversation> selectConversationList(Long userId)
    {
        return mapper.selectConversationList(userId);
    }

    @Override
    public SysArtBotConversation selectConversation(Long conversationId, Long userId)
    {
        SysArtBotConversation conversation = mapper.selectConversation(conversationId, userId);
        if (conversation == null)
        {
            throw new ServiceException("会话不存在或无权访问");
        }
        return conversation;
    }

    @Override
    public SysArtBotConversation createConversation(Long userId, Long modelId)
    {
        SysArtBotModel model = requiredEnabledModel(modelId);
        SysArtBotConversation conversation = new SysArtBotConversation();
        conversation.setUserId(userId);
        conversation.setModelId(model.getModelId());
        conversation.setTitle("新会话");
        mapper.insertConversation(conversation);
        conversation.setModelName(model.getModelName());
        conversation.setModelCode(model.getModelCode());
        return conversation;
    }

    @Override
    @Transactional
    public void deleteConversation(Long conversationId, Long userId)
    {
        selectConversation(conversationId, userId);
        mapper.deleteMessagesByConversationId(conversationId);
        mapper.deleteConversation(conversationId, userId);
    }

    @Override
    public List<SysArtBotMessage> selectMessageList(Long conversationId, Long userId)
    {
        selectConversation(conversationId, userId);
        return mapper.selectMessageList(conversationId);
    }

    @Override
    public List<SysArtBotMessage> selectRecentMessages(Long conversationId, int limit)
    {
        return mapper.selectRecentMessages(conversationId, limit);
    }

    @Override
    public void insertMessage(Long conversationId, String role, String content)
    {
        SysArtBotMessage message = new SysArtBotMessage();
        message.setConversationId(conversationId);
        message.setRole(role);
        message.setContent(content);
        mapper.insertMessage(message);
        mapper.touchConversation(conversationId);
    }

    @Override
    public void titleConversation(Long conversationId, String content)
    {
        String title = content.trim().replaceAll("\\s+", " ");
        mapper.updateConversationTitle(conversationId, title.length() > 30 ? title.substring(0, 30) : title);
    }

    private SysArtBotModel requiredModel(Long modelId)
    {
        SysArtBotModel model = mapper.selectModelById(modelId);
        if (model == null)
        {
            throw new ServiceException("模型不存在");
        }
        return model;
    }

    private SysArtBotModel requiredEnabledModel(Long modelId)
    {
        SysArtBotModel model = requiredModel(modelId);
        if (!"0".equals(model.getStatus()))
        {
            throw new ServiceException("模型已停用");
        }
        return model;
    }

    private void normalize(SysArtBotModel model)
    {
        if (StringUtils.isEmpty(model.getStatus())) model.setStatus("0");
        if (StringUtils.isEmpty(model.getIsDefault())) model.setIsDefault("0");
        if (StringUtils.isNotEmpty(model.getBaseUrl()))
        {
            model.setBaseUrl(model.getBaseUrl().replaceAll("/+$", ""));
        }
    }

    private void maskApiKey(SysArtBotModel model)
    {
        if (model != null && StringUtils.isNotEmpty(model.getApiKey()))
        {
            model.setApiKey(MASKED_KEY);
        }
    }
}
