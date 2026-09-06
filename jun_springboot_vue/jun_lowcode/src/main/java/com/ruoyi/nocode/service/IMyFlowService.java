package com.ruoyi.nocode.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.nocode.domain.bo.ExecBO;
import com.ruoyi.nocode.domain.bo.StartBO;

import javax.servlet.http.HttpServletRequest;

public interface IMyFlowService {

    /**
     * 发起流程
     *
     * @param startBO
     * @return
     */
    public AjaxResult startFlow(StartBO startBO);

    /**
     * 处理流程节点
     *
     * @param execBO
     * @return
     */
    public AjaxResult execFlowNode(ExecBO execBO);

    /**
     * 根据businessid查询表单详情
     *
     * @param businessId
     * @return
     */
    public AjaxResult getDataByBusinessId(String businessId);

    /**
     * 根据formid查询表单数据集
     *
     * @param formId
     * @param request
     * @return
     */
    public TableDataInfo getDataListByFormId(String formId, HttpServletRequest request);


    /**
     * 查询我发起的任务
     *
     * @return
     */
    public TableDataInfo getDataListByUser();

    /**
     * 根据businessId获取流程审批记录
     *
     * @param businessId
     * @return
     */
    public AjaxResult getFlowhistoryByBusinessId(String businessId);

    /**
     * 获取所有用户
     *
     * @return
     */
    public AjaxResult getAllUsers();

    /**
     * 获取所有岗位
     *
     * @return
     */
    public AjaxResult getAllPosts();

}
