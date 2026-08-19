package com.ruoyi.ai.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ai.mapper.VectorDatabaseListMapper;
import com.ruoyi.ai.domain.VectorDatabaseList;
import com.ruoyi.ai.service.IVectorDatabaseListService;

/**
 * 向量库列表Service业务层处理
 * 
 * @author 欧阳日峰
 * @date 2025-02-28
 */
@Service
public class VectorDatabaseListServiceImpl implements IVectorDatabaseListService 
{
    @Autowired
    private VectorDatabaseListMapper vectorDatabaseListMapper;

    /**
     * 查询向量库列表
     * 
     * @param id 向量库列表主键
     * @return 向量库列表
     */
    @Override
    public VectorDatabaseList selectVectorDatabaseListById(Long id)
    {
        return vectorDatabaseListMapper.selectVectorDatabaseListById(id);
    }

    /**
     * 查询向量库列表列表
     * 
     * @param vectorDatabaseList 向量库列表
     * @return 向量库列表
     */
    @Override
    public List<VectorDatabaseList> selectVectorDatabaseListList(VectorDatabaseList vectorDatabaseList)
    {
        return vectorDatabaseListMapper.selectVectorDatabaseListList(vectorDatabaseList);
    }

    /**
     * 新增向量库列表
     * 
     * @param vectorDatabaseList 向量库列表
     * @return 结果
     */
    @Override
    public int insertVectorDatabaseList(VectorDatabaseList vectorDatabaseList)
    {
        return vectorDatabaseListMapper.insertVectorDatabaseList(vectorDatabaseList);
    }

    /**
     * 修改向量库列表
     * 
     * @param vectorDatabaseList 向量库列表
     * @return 结果
     */
    @Override
    public int updateVectorDatabaseList(VectorDatabaseList vectorDatabaseList)
    {
        return vectorDatabaseListMapper.updateVectorDatabaseList(vectorDatabaseList);
    }

    /**
     * 批量删除向量库列表
     * 
     * @param ids 需要删除的向量库列表主键
     * @return 结果
     */
    @Override
    public int deleteVectorDatabaseListByIds(Long[] ids)
    {
        return vectorDatabaseListMapper.deleteVectorDatabaseListByIds(ids);
    }

    /**
     * 删除向量库列表信息
     * 
     * @param id 向量库列表主键
     * @return 结果
     */
    @Override
    public int deleteVectorDatabaseListById(Long id)
    {
        return vectorDatabaseListMapper.deleteVectorDatabaseListById(id);
    }

    @Override
    public VectorDatabaseList selectEnableVectorDatabase() {
        return vectorDatabaseListMapper.selectEnableVectorDatabase();
    }
}
