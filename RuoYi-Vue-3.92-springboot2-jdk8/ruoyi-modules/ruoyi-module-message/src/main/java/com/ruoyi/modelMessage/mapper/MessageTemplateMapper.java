package com.ruoyi.modelMessage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.ruoyi.modelMessage.domain.MessageTemplate;

/**
 * 模版管理Mapper接口
 * 
 * @author ruoyi
 * @date 2024-12-31
 */
public interface MessageTemplateMapper 
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
     * 删除模版管理
     * 
     * @param templateId 模版管理主键
     * @return 结果
     */
    public int deleteMessageTemplateByTemplateId(Long templateId);

    /**
     * 批量删除模版管理
     * 
     * @param templateIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMessageTemplateByTemplateIds(Long[] templateIds);

    /**
     * 根据模板templateCode查询模板信息
     *
     * @param templateCode 模版编码
     * @return 模版对象
     */
    @Select("SELECT template_code,template_variable,template_content FROM `message_template` WHERE template_code = #{templateCode}")
    public MessageTemplate selectMessageTemplateByTemplateCode(String templateCode);

     //查询模版签名
    @Select("SELECT template_id,template_code FROM `message_template`")
    public List<MessageTemplate> selecTemplates();
}
