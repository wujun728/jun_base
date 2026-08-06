package com.ruoyi.flowable.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.flowable.domain.dto.FlowTaskDto;
import com.ruoyi.flowable.domain.vo.FlowQueryVo;
import com.ruoyi.flowable.domain.vo.FlowTaskVo;
import com.ruoyi.flowable.service.IFlowTaskService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 工作流任务管理
 * <p>
 *
 * @author Tony
 * @date 2021-04-03
 */
@Slf4j
@Api(tags = "工作流流程任务管理")
@RestController
@RequestMapping("/flowable/task")
public class FlowTaskController extends BaseController {

    @Autowired
    private IFlowTaskService flowTaskService;

    @ApiOperation("我发起的流程")
    @GetMapping(value = "/myProcess")
    public TableDataInfo myProcess(FlowQueryVo queryVo) {
        List<FlowTaskDto> myProcess = flowTaskService.myProcess(queryVo);
        return getDataTable(myProcess);
    }

    @ApiOperation("取消申请")
    @Log(title = "取消申请", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/stopProcess")
    public AjaxResult stopProcess(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.stopProcess(flowTaskVo);
    }

    @ApiOperation("撤回流程")
    @Log(title = "撤回流程", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/revokeProcess")
    public AjaxResult revokeProcess(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.revokeProcess(flowTaskVo);
    }

    @ApiOperation("获取待办列表")
    @GetMapping(value = "/todoList")
    public TableDataInfo todoList(FlowQueryVo queryVo) {
        return getDataTable(flowTaskService.todoList(queryVo));
    }

    @ApiOperation("获取已办任务")
    @GetMapping(value = "/finishedList")
    public TableDataInfo finishedList(FlowQueryVo queryVo) {
        return getDataTable(flowTaskService.finishedList(queryVo));
    }

    @ApiOperation("流程历史流转记录")
    @GetMapping(value = "/flowRecord")
    public AjaxResult flowRecord(String procInsId, String deployId) {
        return flowTaskService.flowRecord(procInsId, deployId);
    }

    @ApiOperation("根据任务ID查询挂载的表单信息")
    @GetMapping(value = "/getTaskForm")
    public AjaxResult getTaskForm(String taskId) {
        return flowTaskService.getTaskForm(taskId);
    }

    @ApiOperation("流程初始化表单")
    @GetMapping(value = "/flowFormData")
    public AjaxResult flowFormData(String deployId) {
        return flowTaskService.flowFormData(deployId);
    }

    @ApiOperation("获取流程变量")
    @GetMapping(value = "/processVariables/{taskId}")
    public AjaxResult processVariables(@PathVariable String taskId) {
        return flowTaskService.processVariables(taskId);
    }

    @ApiOperation("审批任务")
    @Log(title = "审批任务", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/complete")
    public AjaxResult complete(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.complete(flowTaskVo);
    }

    @ApiOperation("驳回任务")
    @Log(title = "驳回任务", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/reject")
    public AjaxResult taskReject(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.taskReject(flowTaskVo);
        return AjaxResult.success();
    }

    @ApiOperation("退回任务")
    @Log(title = "退回任务", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/return")
    public AjaxResult taskReturn(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.taskReturn(flowTaskVo);
        return AjaxResult.success();
    }

    @ApiOperation("获取所有可回退的节点")
    @PostMapping(value = "/returnList")
    public AjaxResult findReturnTaskList(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.findReturnTaskList(flowTaskVo);
    }

    @ApiOperation("删除任务")
    @Log(title = "删除任务", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/delete")
    public AjaxResult delete(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.deleteTask(flowTaskVo);
        return AjaxResult.success();
    }

    @ApiOperation("认领/签收任务")
    @PostMapping(value = "/claim")
    public AjaxResult claim(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.claim(flowTaskVo);
        return AjaxResult.success();
    }

    @ApiOperation("取消认领/签收任务")
    @PostMapping(value = "/unClaim")
    public AjaxResult unClaim(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.unClaim(flowTaskVo);
        return AjaxResult.success();
    }

    @ApiOperation("委派任务")
    @PostMapping(value = "/delegateTask")
    public AjaxResult delegate(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.delegateTask(flowTaskVo);
        return AjaxResult.success();
    }

    @ApiOperation("任务归还")
    @PostMapping(value = "/resolveTask")
    public AjaxResult resolveTask(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.resolveTask(flowTaskVo);
        return AjaxResult.success();
    }

    @ApiOperation("转办任务")
    @PostMapping(value = "/assignTask")
    public AjaxResult assign(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.assignTask(flowTaskVo);
        return AjaxResult.success();
    }

    @PostMapping(value = "/addMultiInstanceExecution")
    @ApiOperation("多实例加签")
    public AjaxResult addMultiInstanceExecution(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.addMultiInstanceExecution(flowTaskVo);
        return AjaxResult.success("加签成功");
    }

    @PostMapping(value = "/deleteMultiInstanceExecution")
    @ApiOperation("多实例减签")
    public AjaxResult deleteMultiInstanceExecution(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.deleteMultiInstanceExecution(flowTaskVo);
        return AjaxResult.success("减签成功");
    }

    @ApiOperation("获取下一节点")
    @PostMapping(value = "/nextFlowNode")
    public AjaxResult getNextFlowNode(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.getNextFlowNode(flowTaskVo);
    }

    @ApiOperation("流程发起时获取下一节点")
    @PostMapping(value = "/nextFlowNodeByStart")
    public AjaxResult getNextFlowNodeByStart(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.getNextFlowNodeByStart(flowTaskVo);
    }

    /**
     * 生成流程图
     *
     * @param processId 任务ID
     */
    @GetMapping("/diagram/{processId}")
    public void genProcessDiagram(HttpServletResponse response, @PathVariable("processId") String processId) {
        InputStream inputStream = flowTaskService.diagram(processId);
        OutputStream os = null;
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
            response.setContentType("image/png");
            os = response.getOutputStream();
            if (image != null) {
                ImageIO.write(image, "png", os);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取流程执行节点
     *
     * @param procInsId 流程实例编号
     * @param procInsId 任务执行编号
     */
    @GetMapping("/flowViewer/{procInsId}/{executionId}")
    public AjaxResult getFlowViewer(
            @PathVariable("procInsId") String procInsId,
            @PathVariable("executionId") String executionId) {
        return flowTaskService.getFlowViewer(procInsId, executionId);
    }

    /**
     * 流程节点信息
     *
     * @param procInsId 流程实例id
     * @return
     */
    @GetMapping("/flowXmlAndNode")
    public AjaxResult flowXmlAndNode(@RequestParam(value = "procInsId", required = false) String procInsId,
            @RequestParam(value = "deployId", required = false) String deployId) {
        return flowTaskService.flowXmlAndNode(procInsId, deployId);
    }

    /**
     * 流程节点表单
     *
     * @param taskId 流程任务编号
     * @return
     */
    @GetMapping("/flowTaskForm")
    public AjaxResult flowTaskForm(@RequestParam(value = "taskId", required = false) String taskId) throws Exception {
        return flowTaskService.flowTaskForm(taskId);
    }

    /**
     * 流程节点信息
     *
     * @param procInsId 流程实例编号
     * @param elementId 流程节点编号
     * @return
     */
    @GetMapping("/flowTaskInfo")
    public AjaxResult flowTaskInfo(
            @RequestParam(value = "procInsId") String procInsId,
            @RequestParam(value = "elementId") String elementId) {
        return flowTaskService.flowTaskInfo(procInsId, elementId);
    }

}
