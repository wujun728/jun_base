package com.ruoyi.nocode.service.impl;

import com.ruoyi.nocode.domain.dto.ActivitiHighLineDTO;
import com.ruoyi.nocode.service.IActivitiHistoryService;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivitiHistoryServiceImpl implements IActivitiHistoryService {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;


    @Override
    public ActivitiHighLineDTO gethighLine(String instanceId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId).singleResult();
        //获取bpmnModel对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        //因为我们这里只定义了一个Process 所以获取集合中的第一个即可
        Process process = bpmnModel.getProcesses().get(0);
        //获取所有的FlowElement信息
        Collection<FlowElement> flowElements = process.getFlowElements();

        Map<String, String> map = new HashMap<>();
        for (FlowElement flowElement : flowElements) {
            //判断是否是连线
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                String ref = sequenceFlow.getSourceRef();
                String targetRef = sequenceFlow.getTargetRef();
                map.put(ref + targetRef, sequenceFlow.getId());
            }
        }

        //获取流程实例 历史节点(全部)
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .list();
        //各个历史节点   两两组合 key
        Set<String> keyList = new HashSet<>();
        for (HistoricActivityInstance i : list) {
            for (HistoricActivityInstance j : list) {
                if (i != j) {
                    keyList.add(i.getActivityId() + j.getActivityId());
                }
            }
        }
        //高亮连线ID
        Set<String> highLine = new HashSet<>();
        keyList.forEach(s -> highLine.add(map.get(s)));


        //获取流程实例 历史节点（已完成）
        List<HistoricActivityInstance> listFinished = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .finished()
                .list();
        //高亮节点ID
        Set<String> highPoint = new HashSet<>();
        listFinished.forEach(s -> highPoint.add(s.getActivityId()));

        //获取流程实例 历史节点（待办节点）
        List<HistoricActivityInstance> listUnFinished = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .unfinished()
                .list();

        //需要移除的高亮连线
        Set<String> set = new HashSet<>();
        //待办高亮节点
        Set<String> waitingToDo = new HashSet<>();
        listUnFinished.forEach(s -> {
            waitingToDo.add(s.getActivityId());

            for (FlowElement flowElement : flowElements) {
                //判断是否是 用户节点
                if (flowElement instanceof UserTask) {
                    UserTask userTask = (UserTask) flowElement;

                    if (userTask.getId().equals(s.getActivityId())) {
                        List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
                        //因为 高亮连线查询的是所有节点  两两组合 把待办 之后  往外发出的连线 也包含进去了  所以要把高亮待办节点 之后 即出的连线去掉
                        if (outgoingFlows != null && outgoingFlows.size() > 0) {
                            outgoingFlows.forEach(a -> {
                                if (a.getSourceRef().equals(s.getActivityId())) {
                                    set.add(a.getId());
                                }
                            });
                        }
                    }
                }
            }
        });

        highLine.removeAll(set);
        Set<String> iDo = new HashSet<>(); //存放 高亮 我的办理节点
        //当前用户已完成的任务
        List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
//                    .taskAssignee(SecurityUtils.getUsername())
                .finished()
                .processInstanceId(instanceId).list();

        taskInstanceList.forEach(a -> iDo.add(a.getTaskDefinitionKey()));

        ActivitiHighLineDTO activitiHighLineDTO =new ActivitiHighLineDTO();
        activitiHighLineDTO.setHighPoint(highPoint);
        activitiHighLineDTO.setHighLine(highLine);
        activitiHighLineDTO.setWaitingToDo(waitingToDo);
        activitiHighLineDTO.setiDo(iDo);

        return activitiHighLineDTO;
    }
}
