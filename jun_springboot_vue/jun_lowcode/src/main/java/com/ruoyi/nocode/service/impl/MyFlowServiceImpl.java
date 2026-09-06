package com.ruoyi.nocode.service.impl;

import com.mongodb.BasicDBObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.nocode.domain.MyFormData;
import com.ruoyi.nocode.domain.MyWorkflowFormdata;
import com.ruoyi.nocode.domain.bo.ExecBO;
import com.ruoyi.nocode.domain.bo.StartBO;
import com.ruoyi.nocode.service.IMyFlowService;
import com.ruoyi.nocode.service.IMyWorkflowFormdataService;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.mapper.SysPostMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.ruoyi.nocode.constant.FlowConstants.*;

@Service
public class MyFlowServiceImpl implements IMyFlowService {
    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    IMyWorkflowFormdataService myWorkflowFormdataService;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    SysPostMapper sysPostMapper;
    @Autowired
    private HistoryService historyService;

    @Override
    public AjaxResult startFlow(StartBO startBO) {
        String id = UUID.randomUUID().toString();
        MyFormData myFormData = new MyFormData();
        myFormData.setOid(id);
        myFormData.setFormId(startBO.getFormId());
        myFormData.setData(BasicDBObject.parse(startBO.getData()));
        myFormData.setCreateName(SecurityUtils.getLoginUser().getUser().getNickName());
        myFormData.setCreateBy(SecurityUtils.getUsername());
        myFormData.setCreateTime(DateUtils.getNowDate());
        myFormData.setFormName(startBO.getFormName());
        if (!StringUtils.isEmpty(startBO.getProcKey())) {
            ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                    .start()
                    .withProcessDefinitionKey(startBO.getProcKey())
                    .withName(SecurityUtils.getLoginUser().getUser().getNickName() + "的" + startBO.getFormName() + "申请")
                    .withBusinessKey(id)
                    .build());
            myFormData.setBusinessId(id);
            myFormData.setProcKey(startBO.getProcKey());
            myFormData.setInstanceId(processInstance.getId());
            myFormData.setWithProc(PROC_YES);
            myFormData.setStatus(RUNNING);
        } else {
            myFormData.setWithProc(PROC_NO);
            myFormData.setStatus(PASS);
        }
        mongoTemplate.insert(myFormData);
        return AjaxResult.success("流程启动成功");
    }

    @Override
    public AjaxResult execFlowNode(ExecBO execBO) {
        String taskID = execBO.getTaskID();
        Task task = taskRuntime.task(taskID);
        org.activiti.engine.runtime.ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        String processInstanceId = processInstance.getProcessInstanceId();

        HashMap<String, Object> variables = new HashMap<String, Object>();

        variables.put(VAR, execBO.getPass());
        if (task.getAssignee() == null) {
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
        }
        //带参数完成任务
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskID)
                .withVariables(variables)
                .build());

        MyWorkflowFormdata myWorkflowFormdata = new MyWorkflowFormdata();
        myWorkflowFormdata.setId(UUID.randomUUID().toString());
        myWorkflowFormdata.setBusinessKey(execBO.getBusinessKey());
        myWorkflowFormdata.setComment(execBO.getComment());
        myWorkflowFormdata.setPass(execBO.getPass());
        myWorkflowFormdata.setTaskNodeName(execBO.getTaskNodeName());
        myWorkflowFormdata.setCreateName(SecurityUtils.getLoginUser().getUser().getNickName());
        myWorkflowFormdata.setCreateBy(SecurityUtils.getUsername());
        myWorkflowFormdataService.insertMyWorkflowFormdata(myWorkflowFormdata);

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (historicProcessInstance.getEndTime() != null) {
            Update update = new Update();
            if (execBO.getPass() != null && VAR_VALUE_NOT_PASS.equals(execBO.getPass()))
                update.set("status", NOTPASS);
            else
                update.set("status", PASS);
            Query query = new Query();
            Criteria criteria = Criteria.where("businessId").is(execBO.getBusinessKey());
            query.addCriteria(criteria);
            mongoTemplate.updateFirst(query, update, MyFormData.class);
        }
        return AjaxResult.success("处理成功");
    }

    @Override
    public AjaxResult getDataByBusinessId(String businessId) {
        Query query = new Query();
        Criteria criteria = Criteria.where("businessId").is(businessId);
        query.addCriteria(criteria);
        List<MyFormData> myFormDataList = mongoTemplate.find(query, MyFormData.class);
        if (CollectionUtils.isEmpty(myFormDataList))
            return new AjaxResult(HttpStatus.ERROR, "未查询到对应数据");
        else
            return new AjaxResult(HttpStatus.SUCCESS, "ok", myFormDataList.get(0).getData());
    }

    @Override
    public TableDataInfo getDataListByFormId(String formId, HttpServletRequest request) {
        Query query = new Query();
        query.addCriteria(Criteria.where("formId").is(formId));
        query.addCriteria(Criteria.where("status").is(PASS));
        if (!StringUtils.isEmpty(ServletUtils.getParameter("params[beginTime]")) && !StringUtils.isEmpty(ServletUtils.getParameter("params[endTime]"))) {
            Date beginTime = DateUtils.parseDate(ServletUtils.getParameter("params[beginTime]"));
            Date endTime = DateUtils.parseDate(ServletUtils.getParameter("params[endTime]"));
            query.addCriteria(Criteria.where("createTime").gte(beginTime).lte(endTime));
        }
        long totalCount = mongoTemplate.count(query, MyFormData.class);
        mongoPage(query);
        query.with(Sort.by(Sort.Direction.DESC, "createTime"));
        List<MyFormData> myFormDataList = mongoTemplate.find(query, MyFormData.class);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(myFormDataList.stream().map(item -> {
            item.getData().put("_formId", item.getFormId());
            item.getData().put("_createName", item.getCreateName());
            item.getData().put("_createBy", item.getCreateBy());
            item.getData().put("_createTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, item.getCreateTime()));
            return item.getData();
        }).collect(Collectors.toList()));
        rspData.setTotal(totalCount);
        return rspData;
    }

    @Override
    public TableDataInfo getDataListByUser() {
        String userId = SecurityUtils.getUsername();
        Query query = new Query();
        query.addCriteria(Criteria.where("createBy").is(userId));
        query.addCriteria(Criteria.where("withProc").is(PROC_YES));
        long totalCount = mongoTemplate.count(query, MyFormData.class);
        mongoPage(query);
        query.with(Sort.by(Sort.Direction.DESC, "createTime"));
        List<MyFormData> myFormDataList = mongoTemplate.find(query, MyFormData.class);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(myFormDataList);
        rspData.setTotal(totalCount);
        return rspData;
    }

    private void mongoPage(Query query) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
            query.with(pageable);
        }
    }

    @Override
    public AjaxResult getFlowhistoryByBusinessId(String businessId) {
        MyWorkflowFormdata myWorkflowFormdata = new MyWorkflowFormdata();
        myWorkflowFormdata.setBusinessKey(businessId);
        List<MyWorkflowFormdata> list = myWorkflowFormdataService.selectMyWorkflowFormdataList(myWorkflowFormdata);
        return new AjaxResult(HttpStatus.SUCCESS, "ok", list);
    }

    @Override
    public AjaxResult getAllUsers() {
        List<SysUser> list = sysUserMapper.selectUserList(new SysUser());
        return new AjaxResult(HttpStatus.SUCCESS, "ok", list);
    }

    @Override
    public AjaxResult getAllPosts() {
        List<SysPost> list = sysPostMapper.selectPostAll();
        return new AjaxResult(HttpStatus.SUCCESS, "ok", list);
    }

}
