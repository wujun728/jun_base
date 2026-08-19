package com.ruoyi.ai.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ai.mapper.KnowledgeBaseListMapper;
import com.ruoyi.ai.domain.KnowledgeBaseList;
import com.ruoyi.ai.service.IKnowledgeBaseListService;

/**
 * 知识库列表Service业务层处理
 * 
 * @author 欧阳日峰
 * @date 2025-04-10
 */
@Service
public class KnowledgeBaseListServiceImpl implements IKnowledgeBaseListService 
{
    @Autowired
    private KnowledgeBaseListMapper knowledgeBaseListMapper;

    /**
     * 查询知识库列表
     * 
     * @param id 知识库列表主键
     * @return 知识库列表
     */
    @Override
    public KnowledgeBaseList selectKnowledgeBaseListById(Long id)
    {
        return knowledgeBaseListMapper.selectKnowledgeBaseListById(id);
    }

    /**
     * 查询知识库列表列表
     * 
     * @param knowledgeBaseList 知识库列表
     * @return 知识库列表
     */
    @Override
    public List<KnowledgeBaseList> selectKnowledgeBaseListList(KnowledgeBaseList knowledgeBaseList)
    {
        return knowledgeBaseListMapper.selectKnowledgeBaseListList(knowledgeBaseList);
    }

    /**
     * 新增知识库列表
     * 
     * @param knowledgeBaseList 知识库列表
     * @return 结果
     */
    @Override
    public int insertKnowledgeBaseList(KnowledgeBaseList knowledgeBaseList)
    {
        knowledgeBaseList.setCreateTime(DateUtils.getNowDate());
        return knowledgeBaseListMapper.insertKnowledgeBaseList(knowledgeBaseList);
    }

    /**
     * 修改知识库列表
     * 
     * @param knowledgeBaseList 知识库列表
     * @return 结果
     */
    @Override
    public int updateKnowledgeBaseList(KnowledgeBaseList knowledgeBaseList)
    {
        knowledgeBaseList.setUpdateTime(DateUtils.getNowDate());
        return knowledgeBaseListMapper.updateKnowledgeBaseList(knowledgeBaseList);
    }

    /**
     * 批量删除知识库列表
     * 
     * @param ids 需要删除的知识库列表主键
     * @return 结果
     */
    @Override
    public int deleteKnowledgeBaseListByIds(Long[] ids)
    {
        return knowledgeBaseListMapper.deleteKnowledgeBaseListByIds(ids);
    }

    /**
     * 删除知识库列表信息
     * 
     * @param id 知识库列表主键
     * @return 结果
     */
    @Override
    public int deleteKnowledgeBaseListById(Long id)
    {
        return knowledgeBaseListMapper.deleteKnowledgeBaseListById(id);
    }
}
