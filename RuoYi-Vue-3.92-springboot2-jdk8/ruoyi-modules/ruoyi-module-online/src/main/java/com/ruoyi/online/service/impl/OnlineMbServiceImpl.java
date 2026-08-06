package com.ruoyi.online.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.online.domain.OnlineMb;
import com.ruoyi.online.mapper.OnlineMbMapper;
import com.ruoyi.online.service.IOnlineMbService;

/**
 * mybatis在线接口Service业务层处理
 * 
 * @author Dftre
 * @date 2024-01-26
 */
@Service
public class OnlineMbServiceImpl implements IOnlineMbService 
{
    @Autowired
    private OnlineMbMapper onlineMbMapper;

    /**
     * 查询mybatis在线接口
     * 
     * @param mbId mybatis在线接口主键
     * @return mybatis在线接口
     */
    @Override
    public OnlineMb selectOnlineMbByMbId(Long mbId)
    {
        return onlineMbMapper.selectOnlineMbByMbId(mbId);
    }

    /**
     * 查询mybatis在线接口列表
     * 
     * @param onlineMb mybatis在线接口
     * @return mybatis在线接口
     */
    @Override
    public List<OnlineMb> selectOnlineMbList(OnlineMb onlineMb)
    {
        return onlineMbMapper.selectOnlineMbList(onlineMb);
    }

    /**
     * 新增mybatis在线接口
     * 
     * @param onlineMb mybatis在线接口
     * @return 结果
     */
    @Override
    public int insertOnlineMb(OnlineMb onlineMb)
    {
        return onlineMbMapper.insertOnlineMb(onlineMb);
    }

    /**
     * 修改mybatis在线接口
     * 
     * @param onlineMb mybatis在线接口
     * @return 结果
     */
    @Override
    public int updateOnlineMb(OnlineMb onlineMb)
    {
        return onlineMbMapper.updateOnlineMb(onlineMb);
    }

    /**
     * 批量删除mybatis在线接口
     * 
     * @param mbIds 需要删除的mybatis在线接口主键
     * @return 结果
     */
    @Override
    public int deleteOnlineMbByMbIds(Long[] mbIds)
    {
        return onlineMbMapper.deleteOnlineMbByMbIds(mbIds);
    }

    /**
     * 删除mybatis在线接口信息
     * 
     * @param mbId mybatis在线接口主键
     * @return 结果
     */
    @Override
    public int deleteOnlineMbByMbId(Long mbId)
    {
        return onlineMbMapper.deleteOnlineMbByMbId(mbId);
    }
}
