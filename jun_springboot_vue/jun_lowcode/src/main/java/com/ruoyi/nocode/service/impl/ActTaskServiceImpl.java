

package com.ruoyi.nocode.service.impl;

import com.github.pagehelper.Page;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.nocode.domain.MyFormDef;
import com.ruoyi.nocode.domain.dto.ActTaskDTO;
import com.ruoyi.nocode.mapper.MyFormDefMapper;
import com.ruoyi.nocode.service.IActTaskService;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActTaskServiceImpl implements IActTaskService {

    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private MyFormDefMapper myFormDefMapper;

    @Override
    public Page<ActTaskDTO> selectTaskList(PageDomain pageDomain) {
        Page<ActTaskDTO> list = new Page<ActTaskDTO>();
        org.activiti.api.runtime.shared.query.Page<Task> pageTasks = taskRuntime.tasks(
                Pageable.of(
                        (pageDomain.getPageNum() - 1) * pageDomain.getPageSize(),
                        pageDomain.getPageSize()
                ));
        List<Task> tasks = pageTasks.getContent();
        int totalItems = pageTasks.getTotalItems();
        list.setTotal(totalItems);
        if (totalItems != 0) {
            Set<String> processInstanceIdIds = tasks.parallelStream().map(t -> t.getProcessInstanceId()).collect(Collectors.toSet());
            List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceIdIds).list();
            List<ActTaskDTO> actTaskDTOS = tasks.stream()
                    .map(t -> new ActTaskDTO(t, processInstanceList.parallelStream().filter(pi -> t.getProcessInstanceId().equals(pi.getId())).findAny().get()))
                    .collect(Collectors.toList());

            //获取代办任务对应的表单定义
            actTaskDTOS.stream().forEach(actTaskDTO -> {
                MyFormDef myFormDef = new MyFormDef();
                myFormDef.setRefProcKey(actTaskDTO.getDefinitionKey());
                List<MyFormDef> myFormDefs = myFormDefMapper.selectMyFormDefList(myFormDef);
                if (!CollectionUtils.isEmpty(myFormDefs))
                    actTaskDTO.setFormDef(myFormDefs.get(0).getDefination());
            });

            list.addAll(actTaskDTOS);

        }
        return list;
    }
}
