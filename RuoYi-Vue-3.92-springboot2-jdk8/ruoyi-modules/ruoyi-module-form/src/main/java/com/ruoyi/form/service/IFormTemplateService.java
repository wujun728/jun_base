package com.ruoyi.form.service;

import java.util.List;
import com.ruoyi.form.domain.FormTemplate;

/**
 * 单模板Service接口
 * 
 * @author ruoyi
 * @date 2025-05-12
 */
public interface IFormTemplateService 
{
    /**
     * 查询单模板
     * 
     * @param formId 单模板主键
     * @return 单模板
     */
    public FormTemplate selectFormTemplateByFormId(Long formId);

    /**
     * 查询单模板列表
     * 
     * @param formTemplate 单模板
     * @return 单模板集合
     */
    public List<FormTemplate> selectFormTemplateList(FormTemplate formTemplate);

    /**
     * 新增单模板
     * 
     * @param formTemplate 单模板
     * @return 结果
     */
    public int insertFormTemplate(FormTemplate formTemplate);

    /**
     * 修改单模板
     * 
     * @param formTemplate 单模板
     * @return 结果
     */
    public int updateFormTemplate(FormTemplate formTemplate);

    /**
     * 批量删除单模板
     * 
     * @param formIds 需要删除的单模板主键集合
     * @return 结果
     */
    public int deleteFormTemplateByFormIds(Long[] formIds);

    /**
     * 删除单模板信息
     * 
     * @param formId 单模板主键
     * @return 结果
     */
    public int deleteFormTemplateByFormId(Long formId);
}
