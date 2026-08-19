package com.ruoyi.ai.mapper;

import java.util.List;
import com.ruoyi.ai.domain.VectorDatabaseList;

/**
 * 向量库列表Mapper接口
 * 
 * @author 欧阳日峰
 * @date 2025-02-28
 */
public interface VectorDatabaseListMapper 
{
    /**
     * 查询向量库列表
     * 
     * @param id 向量库列表主键
     * @return 向量库列表
     */
    public VectorDatabaseList selectVectorDatabaseListById(Long id);

    /**
     * 查询向量库列表列表
     * 
     * @param vectorDatabaseList 向量库列表
     * @return 向量库列表集合
     */
    public List<VectorDatabaseList> selectVectorDatabaseListList(VectorDatabaseList vectorDatabaseList);

    /**
     * 新增向量库列表
     * 
     * @param vectorDatabaseList 向量库列表
     * @return 结果
     */
    public int insertVectorDatabaseList(VectorDatabaseList vectorDatabaseList);

    /**
     * 修改向量库列表
     * 
     * @param vectorDatabaseList 向量库列表
     * @return 结果
     */
    public int updateVectorDatabaseList(VectorDatabaseList vectorDatabaseList);

    /**
     * 删除向量库列表
     * 
     * @param id 向量库列表主键
     * @return 结果
     */
    public int deleteVectorDatabaseListById(Long id);

    /**
     * 批量删除向量库列表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVectorDatabaseListByIds(Long[] ids);

    VectorDatabaseList selectEnableVectorDatabase();
}
