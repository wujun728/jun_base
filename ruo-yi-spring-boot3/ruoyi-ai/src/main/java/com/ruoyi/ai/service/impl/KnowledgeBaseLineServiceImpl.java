package com.ruoyi.ai.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ai.mapper.KnowledgeBaseLineMapper;
import com.ruoyi.ai.domain.KnowledgeBaseLine;
import com.ruoyi.ai.service.IKnowledgeBaseLineService;

/**
 * 知识库单栏Service业务层处理
 * 
 * @author 欧阳日峰
 * @date 2025-04-10
 */
@Service
public class KnowledgeBaseLineServiceImpl implements IKnowledgeBaseLineService 
{
    @Autowired
    private KnowledgeBaseLineMapper knowledgeBaseLineMapper;

    /**
     * 查询知识库单栏
     * 
     * @param id 知识库单栏主键
     * @return 知识库单栏
     */
    @Override
    public KnowledgeBaseLine selectKnowledgeBaseLineById(Long id)
    {
        return knowledgeBaseLineMapper.selectKnowledgeBaseLineById(id);
    }

    /**
     * 查询知识库单栏列表
     * 
     * @param knowledgeBaseLine 知识库单栏
     * @return 知识库单栏
     */
    @Override
    public List<KnowledgeBaseLine> selectKnowledgeBaseLineList(KnowledgeBaseLine knowledgeBaseLine)
    {
        return knowledgeBaseLineMapper.selectKnowledgeBaseLineList(knowledgeBaseLine);
    }

    /**
     * 新增知识库单栏
     * 
     * @param knowledgeBaseLine 知识库单栏
     * @return 结果
     */
    @Override
    public int insertKnowledgeBaseLine(KnowledgeBaseLine knowledgeBaseLine)
    {
        knowledgeBaseLine.setCreateTime(DateUtils.getNowDate());
        return knowledgeBaseLineMapper.insertKnowledgeBaseLine(knowledgeBaseLine);
    }

    /**
     * 修改知识库单栏
     * 
     * @param knowledgeBaseLine 知识库单栏
     * @return 结果
     */
    @Override
    public int updateKnowledgeBaseLine(KnowledgeBaseLine knowledgeBaseLine)
    {
        knowledgeBaseLine.setUpdateTime(DateUtils.getNowDate());
        return knowledgeBaseLineMapper.updateKnowledgeBaseLine(knowledgeBaseLine);
    }

    /**
     * 批量删除知识库单栏
     * 
     * @param ids 需要删除的知识库单栏主键
     * @return 结果
     */
    @Override
    public int deleteKnowledgeBaseLineByIds(Long[] ids)
    {
        return knowledgeBaseLineMapper.deleteKnowledgeBaseLineByIds(ids);
    }

    /**
     * 删除知识库单栏信息
     * 
     * @param id 知识库单栏主键
     * @return 结果
     */
    @Override
    public int deleteKnowledgeBaseLineById(Long id)
    {
        return knowledgeBaseLineMapper.deleteKnowledgeBaseLineById(id);
    }

    @Override
    public List<KnowledgeBaseLine> getLineInfo(Long id) {
        return knowledgeBaseLineMapper.getLineInfo(id);
    }
}
