package com.ruoyi.nocode.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.nocode.domain.MyWorkflowFormdata;
import com.ruoyi.nocode.mapper.MyWorkflowFormdataMapper;
import com.ruoyi.nocode.service.IMyWorkflowFormdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审批记录Service业务层处理
 *
 * @date 2022-07-29
 */
@Service
public class MyWorkflowFormdataServiceImpl implements IMyWorkflowFormdataService
{
    @Autowired
    private MyWorkflowFormdataMapper myWorkflowFormdataMapper;

    /**
     * 查询审批记录
     *
     * @param id 审批记录ID
     * @return 审批记录
     */
    @Override
    public MyWorkflowFormdata selectMyWorkflowFormdataById(String id)
    {
        return myWorkflowFormdataMapper.selectMyWorkflowFormdataById(id);
    }

    /**
     * 查询审批记录列表
     *
     * @param myWorkflowFormdata 审批记录
     * @return 审批记录
     */
    @Override
    public List<MyWorkflowFormdata> selectMyWorkflowFormdataList(MyWorkflowFormdata myWorkflowFormdata)
    {
        return myWorkflowFormdataMapper.selectMyWorkflowFormdataList(myWorkflowFormdata);
    }

    /**
     * 新增审批记录
     *
     * @param myWorkflowFormdata 审批记录
     * @return 结果
     */
    @Override
    public int insertMyWorkflowFormdata(MyWorkflowFormdata myWorkflowFormdata)
    {
        myWorkflowFormdata.setCreateTime(DateUtils.getNowDate());
        return myWorkflowFormdataMapper.insertMyWorkflowFormdata(myWorkflowFormdata);
    }

    /**
     * 修改审批记录
     *
     * @param myWorkflowFormdata 审批记录
     * @return 结果
     */
    @Override
    public int updateMyWorkflowFormdata(MyWorkflowFormdata myWorkflowFormdata)
    {
        myWorkflowFormdata.setUpdateTime(DateUtils.getNowDate());
        return myWorkflowFormdataMapper.updateMyWorkflowFormdata(myWorkflowFormdata);
    }

    /**
     * 批量删除审批记录
     *
     * @param ids 需要删除的审批记录ID
     * @return 结果
     */
    @Override
    public int deleteMyWorkflowFormdataByIds(String[] ids)
    {
        return myWorkflowFormdataMapper.deleteMyWorkflowFormdataByIds(ids);
    }

    /**
     * 删除审批记录信息
     *
     * @param id 审批记录ID
     * @return 结果
     */
    @Override
    public int deleteMyWorkflowFormdataById(String id)
    {
        return myWorkflowFormdataMapper.deleteMyWorkflowFormdataById(id);
    }
}
