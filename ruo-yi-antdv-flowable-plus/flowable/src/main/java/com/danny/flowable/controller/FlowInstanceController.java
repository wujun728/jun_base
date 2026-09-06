package com.danny.flowable.controller;


import com.danny.common.core.domain.AjaxResult;
import com.danny.flowable.domain.vo.FlowTaskVo;

import com.danny.flowable.service.IFlowInstanceService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * <p>工作流流程实例管理<p>
 *
 * @author XuanXuan
 * @date 2021-04-03
 */
@Slf4j
//@Api(tags = "工作流流程实例管理")
@RestController
@RequestMapping("/flowable/instance")
public class FlowInstanceController {

    @Autowired
    private IFlowInstanceService flowInstanceService;

//    @ApiOperation(value = "根据流程定义id启动流程实例")@ApiParam(value = "流程定义id")@ApiParam(value = "变量集合,json对象")
    @PostMapping("/startBy/{procDefId}")
    public AjaxResult startById( @PathVariable(value = "procDefId") String procDefId,
                                 @RequestParam Map<String, Object> variables) {
        return flowInstanceService.startProcessInstanceById(procDefId, variables);

    }


//    @ApiOperation(value = "激活或挂起流程实例")@ApiParam(value = "1:激活,2:挂起", required = true) @ApiParam(value = "流程实例ID", required = true)
    @PostMapping(value = "/updateState")
    public AjaxResult updateState( @RequestParam Integer state,
                                   @RequestParam String instanceId) {
        flowInstanceService.updateState(state,instanceId);
        return AjaxResult.success();
    }

//    @ApiOperation("结束流程实例")
    @PostMapping(value = "/stopProcessInstance")
    public AjaxResult stopProcessInstance(@RequestBody FlowTaskVo flowTaskVo) {
        flowInstanceService.stopProcessInstance(flowTaskVo);
        return AjaxResult.success();
    }

//    @ApiOperation(value = "删除流程实例")@ApiParam(value = "流程实例ID", required = true) @ApiParam(value = "删除原因")
    @DeleteMapping(value = "/delete")
    public AjaxResult delete( @RequestParam String instanceId,
                              @RequestParam(required = false) String deleteReason) {
        flowInstanceService.delete(instanceId,deleteReason);
        return AjaxResult.success();
    }


}
