package com.ruoyi.modelMessage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.modelMessage.domain.MessageTemplate;
import com.ruoyi.modelMessage.mapper.MessageTemplateMapper;
import com.ruoyi.modelMessage.service.IMessageTemplateService;


/**
 * 模版管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-12-31
 */
@Service
public class MessageTemplateServiceImpl implements IMessageTemplateService 
{
    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    /**
     * 查询模版管理
     * 
     * @param templateId 模版管理主键
     * @return 模版管理
     */
    @Override
    public MessageTemplate selectMessageTemplateByTemplateId(Long templateId)
    {
        return messageTemplateMapper.selectMessageTemplateByTemplateId(templateId);
    }

    /**
     * 查询模版管理列表
     * 
     * @param messageTemplate 模版管理
     * @return 模版管理
     */
    @Override
    public List<MessageTemplate> selectMessageTemplateList(MessageTemplate messageTemplate)
    {
        return messageTemplateMapper.selectMessageTemplateList(messageTemplate);
    }

    /**
     * 新增模版管理
     * 
     * @param messageTemplate 模版管理
     * @return 结果
     */
    @Override
    public int insertMessageTemplate(MessageTemplate messageTemplate)
    {
        messageTemplate.setCreateBy(SecurityUtils.getUsername());
        messageTemplate.setCreateTime(DateUtils.getNowDate());
        messageTemplate.setUpdateBy(SecurityUtils.getUsername());
        messageTemplate.setUpdateTime(DateUtils.getNowDate());
        return messageTemplateMapper.insertMessageTemplate(messageTemplate);
    }

    /**
     * 修改模版管理
     * 
     * @param messageTemplate 模版管理
     * @return 结果
     */
    @Override
    public int updateMessageTemplate(MessageTemplate messageTemplate)
    {
        messageTemplate.setUpdateBy(SecurityUtils.getUsername());
        messageTemplate.setUpdateTime(DateUtils.getNowDate());
        return messageTemplateMapper.updateMessageTemplate(messageTemplate);
    }

    /**
     * 批量删除模版管理
     * 
     * @param templateIds 需要删除的模版管理主键
     * @return 结果
     */
    @Override
    public int deleteMessageTemplateByTemplateIds(Long[] templateIds)
    {
        return messageTemplateMapper.deleteMessageTemplateByTemplateIds(templateIds);
    }

    /**
     * 删除模版管理信息
     * 
     * @param templateId 模版管理主键
     * @return 结果
     */
    @Override
    public int deleteMessageTemplateByTemplateId(Long templateId)
    {
        return messageTemplateMapper.deleteMessageTemplateByTemplateId(templateId);
    }

    // 查询模版签名
    @Override
    public List<MessageTemplate> selecTemplates() {
        List<MessageTemplate> templates = messageTemplateMapper.selecTemplates();
        return templates;
    }
}
