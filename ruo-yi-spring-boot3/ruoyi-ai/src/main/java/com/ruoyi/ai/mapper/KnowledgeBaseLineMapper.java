package com.ruoyi.ai.mapper;

import java.util.List;
import com.ruoyi.ai.domain.KnowledgeBaseLine;

/**
 * 知识库单栏Mapper接口
 * 
 * @author 欧阳日峰
 * @date 2025-04-10
 */
public interface KnowledgeBaseLineMapper 
{
    /**
     * 查询知识库单栏
     * 
     * @param id 知识库单栏主键
     * @return 知识库单栏
     */
    public KnowledgeBaseLine selectKnowledgeBaseLineById(Long id);

    /**
     * 查询知识库单栏列表
     * 
     * @param knowledgeBaseLine 知识库单栏
     * @return 知识库单栏集合
     */
    public List<KnowledgeBaseLine> selectKnowledgeBaseLineList(KnowledgeBaseLine knowledgeBaseLine);

    /**
     * 新增知识库单栏
     * 
     * @param knowledgeBaseLine 知识库单栏
     * @return 结果
     */
    public int insertKnowledgeBaseLine(KnowledgeBaseLine knowledgeBaseLine);

    /**
     * 修改知识库单栏
     * 
     * @param knowledgeBaseLine 知识库单栏
     * @return 结果
     */
    public int updateKnowledgeBaseLine(KnowledgeBaseLine knowledgeBaseLine);

    /**
     * 删除知识库单栏
     * 
     * @param id 知识库单栏主键
     * @return 结果
     */
    public int deleteKnowledgeBaseLineById(Long id);

    /**
     * 批量删除知识库单栏
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKnowledgeBaseLineByIds(Long[] ids);

    List<KnowledgeBaseLine> getLineInfo(Long id);
}
