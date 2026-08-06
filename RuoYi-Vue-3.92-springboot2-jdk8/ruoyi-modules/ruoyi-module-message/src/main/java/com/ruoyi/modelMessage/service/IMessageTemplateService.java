package com.ruoyi.modelMessage.service;

import java.util.List;

import com.ruoyi.modelMessage.domain.MessageTemplate;

/**
 * 模版管理Service接口
 * 
 * @author ruoyi
 * @date 2024-12-31
 */
public interface IMessageTemplateService 
{
    /**
     * 查询模版管理
     * 
     * @param templateId 模版管理主键
     * @return 模版管理
     */
    public MessageTemplate selectMessageTemplateByTemplateId(Long templateId);

    /**
     * 查询模版管理列表
     * 
     * @param messageTemplate 模版管理
     * @return 模版管理集合
     */
    public List<MessageTemplate> selectMessageTemplateList(MessageTemplate messageTemplate);

    /**
     * 新增模版管理
     * 
     * @param messageTemplate 模版管理
     * @return 结果
     */
    public int insertMessageTemplate(MessageTemplate messageTemplate);

    /**
     * 修改模版管理
     * 
     * @param messageTemplate 模版管理
     * @return 结果
     */
    public int updateMessageTemplate(MessageTemplate messageTemplate);

    /**
     * 批量删除模版管理
     * 
     * @param templateIds 需要删除的模版管理主键集合
     * @return 结果
     */
    public int deleteMessageTemplateByTemplateIds(Long[] templateIds);

    /**
     * 删除模版管理信息
     * 
     * @param templateId 模版管理主键
     * @return 结果
     */
    public int deleteMessageTemplateByTemplateId(Long templateId);

    // 查询模版签名
    public List<MessageTemplate> selecTemplates();
}
