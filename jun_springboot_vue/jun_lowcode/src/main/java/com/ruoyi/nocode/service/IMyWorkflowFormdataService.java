package com.ruoyi.nocode.service;


import com.ruoyi.nocode.domain.MyWorkflowFormdata;

import java.util.List;

/**
 * 审批记录Service接口
 *
 * @date 2022-07-29
 */
public interface IMyWorkflowFormdataService
{
    /**
     * 查询审批记录
     *
     * @param id 审批记录ID
     * @return 审批记录
     */
    public MyWorkflowFormdata selectMyWorkflowFormdataById(String id);

    /**
     * 查询审批记录列表
     *
     * @param myWorkflowFormdata 审批记录
     * @return 审批记录集合
     */
    public List<MyWorkflowFormdata> selectMyWorkflowFormdataList(MyWorkflowFormdata myWorkflowFormdata);

    /**
     * 新增审批记录
     *
     * @param myWorkflowFormdata 审批记录
     * @return 结果
     */
    public int insertMyWorkflowFormdata(MyWorkflowFormdata myWorkflowFormdata);

    /**
     * 修改审批记录
     *
     * @param myWorkflowFormdata 审批记录
     * @return 结果
     */
    public int updateMyWorkflowFormdata(MyWorkflowFormdata myWorkflowFormdata);

    /**
     * 批量删除审批记录
     *
     * @param ids 需要删除的审批记录ID
     * @return 结果
     */
    public int deleteMyWorkflowFormdataByIds(String[] ids);

    /**
     * 删除审批记录信息
     *
     * @param id 审批记录ID
     * @return 结果
     */
    public int deleteMyWorkflowFormdataById(String id);
}
