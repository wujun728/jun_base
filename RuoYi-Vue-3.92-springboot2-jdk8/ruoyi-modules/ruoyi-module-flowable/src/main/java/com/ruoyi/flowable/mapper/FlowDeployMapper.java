package com.ruoyi.flowable.mapper;

import java.util.List;

import com.ruoyi.flowable.domain.dto.FlowProcDefDto;

/**
 * 流程定义查询
 *
 * @author Tony
 * @email
 * @date 2022/1/29 5:44 下午
 **/
public interface FlowDeployMapper {

    /**
     * 流程定义列表
     * 
     * @param name
     * @return
     */
    List<FlowProcDefDto> selectDeployList(String name);
}
