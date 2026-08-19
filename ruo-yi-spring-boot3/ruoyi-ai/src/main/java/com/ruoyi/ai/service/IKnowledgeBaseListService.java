package com.ruoyi.ai.service;

import java.util.List;
import com.ruoyi.ai.domain.KnowledgeBaseList;

/**
 * 知识库列表Service接口
 * 
 * @author 欧阳日峰
 * @date 2025-04-10
 */
public interface IKnowledgeBaseListService 
{
    /**
     * 查询知识库列表
     * 
     * @param id 知识库列表主键
     * @return 知识库列表
     */
    public KnowledgeBaseList selectKnowledgeBaseListById(Long id);

    /**
     * 查询知识库列表列表
     * 
     * @param knowledgeBaseList 知识库列表
     * @return 知识库列表集合
     */
    public List<KnowledgeBaseList> selectKnowledgeBaseListList(KnowledgeBaseList knowledgeBaseList);

    /**
     * 新增知识库列表
     * 
     * @param knowledgeBaseList 知识库列表
     * @return 结果
     */
    public int insertKnowledgeBaseList(KnowledgeBaseList knowledgeBaseList);

    /**
     * 修改知识库列表
     * 
     * @param knowledgeBaseList 知识库列表
     * @return 结果
     */
    public int updateKnowledgeBaseList(KnowledgeBaseList knowledgeBaseList);

    /**
     * 批量删除知识库列表
     * 
     * @param ids 需要删除的知识库列表主键集合
     * @return 结果
     */
    public int deleteKnowledgeBaseListByIds(Long[] ids);

    /**
     * 删除知识库列表信息
     * 
     * @param id 知识库列表主键
     * @return 结果
     */
    public int deleteKnowledgeBaseListById(Long id);
}
