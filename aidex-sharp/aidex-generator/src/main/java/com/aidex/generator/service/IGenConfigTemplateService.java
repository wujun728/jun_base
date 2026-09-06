package com.aidex.generator.service;

import com.aidex.common.core.service.BaseService;
import com.aidex.generator.domain.GenConfigTemplate;

import java.util.List;

/**
 * 模板配置Service接口
 *
 * @author ruoyi
 * @date 2021-03-06
 */
public interface IGenConfigTemplateService extends BaseService<GenConfigTemplate>
{
    /**
     * 批量删除模板配置
     *
     * @param ids 需要删除的模板配置ID
     * @return 结果
     */
    public int deleteGenConfigTemplateByIds(String[] ids);

    /**
     * 获取最大编号
     * @param genConfigTemplate
     * @return
     */
    public int findMaxSort(GenConfigTemplate genConfigTemplate);

    /**
     * 校验模板名称的唯一性
     * @param genConfigTemplate
     */
    public void checkTemplateNameUnique(GenConfigTemplate genConfigTemplate);

}