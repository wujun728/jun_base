package com.ruoyi.nocode.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.nocode.domain.bo.ExecBO;
import com.ruoyi.nocode.domain.bo.StartBO;
import com.ruoyi.nocode.domain.dto.ActTaskDTO;
import com.ruoyi.nocode.domain.dto.ActivitiHighLineDTO;
import com.ruoyi.nocode.service.IActTaskService;
import com.ruoyi.nocode.service.IActivitiHistoryService;
import com.ruoyi.nocode.service.IMyFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/nocode/flow")
public class FlowController extends BaseController {

    @Autowired
    private IActivitiHistoryService activitiHistoryService;
    @Autowired
    private IActTaskService actTaskService;
    @Autowired
    private IMyFlowService myFlowService;

    /**
     * 发起流程
     *
     * @param startBO
     * @return
     */
    @PostMapping("/start")
    public AjaxResult add(@RequestBody StartBO startBO) {
        return myFlowService.startFlow(startBO);
    }

    /**
     * 处理流程节点
     *
     * @param execBO
     * @return
     */
    @PostMapping("/exec")
    public AjaxResult exec(@RequestBody ExecBO execBO) {
        return myFlowService.execFlowNode(execBO);
    }

    /**
     * 查询表单详情
     *
     * @param businessId
     * @return
     */
    @GetMapping("/data/{businessId}")
    public AjaxResult data(@PathVariable("businessId") String businessId) {
        return myFlowService.getDataByBusinessId(businessId);
    }

    /**
     * 查询表单数据集
     *
     * @param formId
     * @param request
     * @return
     */
    @GetMapping("/data/list/{formId}")
    public TableDataInfo dataList(@PathVariable("formId") String formId, HttpServletRequest request) {
        return myFlowService.getDataListByFormId(formId, request);
    }

    /**
     * 查询我发起的任务
     *
     * @return
     */
    @GetMapping("/data/list/user")
    public TableDataInfo dataListUser() {
        return myFlowService.getDataListByUser();
    }

    /**
     * 获取流程审批记录
     *
     * @param businessId
     * @return
     */
    @GetMapping("/history/{businessId}")
    public AjaxResult history(@PathVariable("businessId") String businessId) {
        return myFlowService.getFlowhistoryByBusinessId(businessId);
    }

    /**
     * 获取所有用户
     *
     * @return
     */
    @Anonymous
    @GetMapping("/api/users")
    public AjaxResult users() {
        return myFlowService.getAllUsers();
    }

    /**
     * 获取所有岗位
     *
     * @return
     */
    @Anonymous
    @GetMapping("/api/posts")
    public AjaxResult posts() {
        return myFlowService.getAllPosts();
    }

    /**
     * 流程图高亮
     *
     * @param instanceId
     * @return
     */
    @Anonymous
    @GetMapping("/gethighLine")
    public AjaxResult gethighLine(@RequestParam("instanceId") String instanceId) {
        ActivitiHighLineDTO activitiHighLineDTO = activitiHistoryService.gethighLine(instanceId);
        return AjaxResult.success(activitiHighLineDTO);
    }

    /**
     * 获取我的代办任务
     *
     * @return
     */
    @GetMapping(value = "/taskList")
    public TableDataInfo getTasks() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        com.github.pagehelper.Page<ActTaskDTO> hashMaps = actTaskService.selectTaskList(pageDomain);
        return getDataTable(hashMaps);
    }

}
