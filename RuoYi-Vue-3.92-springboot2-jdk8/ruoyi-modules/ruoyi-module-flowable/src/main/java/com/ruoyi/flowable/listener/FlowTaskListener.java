package com.ruoyi.flowable.listener;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 任务监听器
 *
 * create（创建）:在任务被创建且所有的任务属性设置完成后才触发
 * assignment（指派）：在任务被分配给某个办理人之后触发
 * complete（完成）：在配置了监听器的上一个任务完成时触发
 * delete（删除）：在任务即将被删除前触发。请注意任务由completeTask正常完成时也会触发
 *
 * @author Tony
 * @date 2021/4/20
 */
@Slf4j
@Component
public class FlowTaskListener implements TaskListener {

    /**
     * 流程设计器添加的参数
     */
    private Expression param;

    @Override
    public void notify(DelegateTask delegateTask) {

        // 获取事件类型 delegateTask.getEventName(),可以通过监听器给任务执行人发送相应的通知消息
        log.info("任务监听器:{} 参数:{}", delegateTask, param.getValue(delegateTask));

    }

}
