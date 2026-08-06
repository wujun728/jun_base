package com.ruoyi.online.mapper;

import java.util.List;

import com.ruoyi.online.domain.OnlineMb;

/**
 * mybatis在线接口Mapper接口
 * 
 * @author Dftre
 * @date 2024-01-26
 */
public interface OnlineMbMapper 
{
    /**
     * 查询mybatis在线接口
     * 
     * @param mbId mybatis在线接口主键
     * @return mybatis在线接口
     */
    public OnlineMb selectOnlineMbByMbId(Long mbId);

    /**
     * 查询mybatis在线接口列表
     * 
     * @param onlineMb mybatis在线接口
     * @return mybatis在线接口集合
     */
    public List<OnlineMb> selectOnlineMbList(OnlineMb onlineMb);

    /**
     * 新增mybatis在线接口
     * 
     * @param onlineMb mybatis在线接口
     * @return 结果
     */
    public int insertOnlineMb(OnlineMb onlineMb);

    /**
     * 修改mybatis在线接口
     * 
     * @param onlineMb mybatis在线接口
     * @return 结果
     */
    public int updateOnlineMb(OnlineMb onlineMb);

    /**
     * 删除mybatis在线接口
     * 
     * @param mbId mybatis在线接口主键
     * @return 结果
     */
    public int deleteOnlineMbByMbId(Long mbId);

    /**
     * 批量删除mybatis在线接口
     * 
     * @param mbIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOnlineMbByMbIds(Long[] mbIds);
}
