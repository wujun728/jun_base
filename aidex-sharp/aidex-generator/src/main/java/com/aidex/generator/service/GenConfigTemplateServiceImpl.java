package com.aidex.generator.service;

import com.github.pagehelper.PageInfo;
import com.aidex.common.core.service.BaseServiceImpl;
import com.aidex.common.exception.BizException;
import com.aidex.common.utils.NumberUtils;
import com.aidex.generator.domain.GenConfigTemplate;
import com.aidex.generator.mapper.GenConfigTemplateMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;


    /**
     * 模板配置Service业务层处理
     *
     * @author ruoyi
     * @date 2021-03-06
     */
    @Transactional(readOnly = true)
    @Service
    public class GenConfigTemplateServiceImpl  extends BaseServiceImpl<GenConfigTemplateMapper, GenConfigTemplate> implements IGenConfigTemplateService
    {

        @Override
        public GenConfigTemplate get(GenConfigTemplate genConfigTemplate) {
            return super.get(genConfigTemplate);
        }

        @Override
        public List<GenConfigTemplate> findList(GenConfigTemplate genConfigTemplate) {
            return super.findList(genConfigTemplate);
        }

        @Override
        public PageInfo<GenConfigTemplate> findPage(GenConfigTemplate genConfigTemplate) {
            return super.findPage(genConfigTemplate);
        }

        @Override
        public boolean save(GenConfigTemplate genConfigTemplate) {
            checkTemplateNameUnique(genConfigTemplate);
            return super.save(genConfigTemplate);
        }

        @Override
        public boolean remove(GenConfigTemplate genConfigTemplate) {
            return super.remove(genConfigTemplate);
        }

        /**
         * 校验模板名称的唯一性
         * @param genConfigTemplate
         */
        @Override
        public void checkTemplateNameUnique(GenConfigTemplate genConfigTemplate) {
            GenConfigTemplate genConfigTemplateUnique = new GenConfigTemplate();
            genConfigTemplateUnique.setNotEqualId(genConfigTemplate.getId());
            genConfigTemplateUnique.setTemplateName(genConfigTemplate.getTemplateName());
            if (!CollectionUtils.isEmpty(mapper.findListWithUnique(genConfigTemplateUnique))) {
                throw new BizException("模板名称已存在，请重新输入！");
            }
        }

        /**
         * 批量删除模板配置
         *
         * @param ids 需要删除的模板配置ID
         * @return 结果
         */
        @Override
        @Transactional(readOnly = false)
        public int deleteGenConfigTemplateByIds(String[] ids)
        {
            return mapper.deleteGenConfigTemplateByIds(ids);
        }

        /**
         * 获取最大编号
         * @param genConfigTemplate
         * @return
         */
        @Override
        public int findMaxSort(GenConfigTemplate genConfigTemplate) {
            return NumberUtils.nextOrder(mapper.findMaxSort(genConfigTemplate));
        }
    }